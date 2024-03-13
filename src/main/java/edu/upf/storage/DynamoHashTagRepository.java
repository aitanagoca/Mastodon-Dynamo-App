package edu.upf.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
////
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
////

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
/////
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.github.tukaaa.model.SimplifiedTweetWithHashtags;

import edu.upf.model.HashTagCount;
import edu.upf.model.HashTagCountComparator;

public class DynamoHashTagRepository implements IHashtagRepository, Serializable {

  final static String endpoint = "dynamodb.us-east-1.amazonaws.com";
  final static String region = "us-east-1";

  /////
  private final String HASH_TAG = "hashtag";
  private final String LANGUAGE = "language";
  private final String COUNT = "count";
  private final String TWEET_IDS = "tweetIds";

  final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
      .withEndpointConfiguration(
          new AwsClientBuilder.EndpointConfiguration(endpoint, region)
      ).withCredentials(new ProfileCredentialsProvider("default"))
      .build();
  final DynamoDB dynamoDB = new DynamoDB(client);
  final Table dynamoDBTable = dynamoDB.getTable("LsdsTwitterHashtags");
  /////

  @Override
  public void write(SimplifiedTweetWithHashtags h) {
    // TODO IMPLEMENT ME
    
    // Iterate over each hashtag in the tweet
    for (String hashtag : h.getHashtags()) {
        
      // Check if the hashtag already exists in the DynamoDB table
      Item existingEntry = dynamoDBTable.getItem(HASH_TAG, hashtag);

      if (existingEntry != null) {
        // Increment count and add tweet ID to existing entry
        Long count = existingEntry.getLong(COUNT) + 1;
        Set<String> tweetIds = existingEntry.getStringSet(TWEET_IDS);
        tweetIds.add(String.valueOf(h.getTweetId()));

        // Update existing entry in DynamoDB
        existingEntry.withStringSet(TWEET_IDS, tweetIds);
        existingEntry.withLong(COUNT, count);
        dynamoDBTable.putItem(existingEntry);
      }
      
      else {
        // Create a new entry if the hashtag does not exist
        String languageCondition = h.getLanguage() != null ? h.getLanguage() : "not specified";
        Item newEntry = new Item()
          .withPrimaryKey(HASH_TAG, hashtag)
          .withString(LANGUAGE, languageCondition)
          .withLong(COUNT, 1L)
          .withStringSet(TWEET_IDS, String.valueOf(h.getTweetId()));

        // Save the new entry to the DynamoDB table
        dynamoDBTable.putItem(newEntry);
      }
    }
  }


  @Override
  public List<HashTagCount> readTop10(String lang) {
    // TODO: Implement this method

    // Map to store hashtag counts
    Map<String, Long> hashtagCountsMap = new HashMap<>();

    // Scan the DynamoDB table for items with the specified language
    dynamoDBTable.scan().forEach(item -> {
      if (item.getString(LANGUAGE).equals(lang)) {
          hashtagCountsMap.put(item.getString(HASH_TAG), item.getLong(COUNT));
      }
    });

    // Sort the hashtags based on their appearance count in descending order
    List<HashTagCount> top10HashTags = hashtagCountsMap.entrySet().stream()
      .map(entry -> new HashTagCount(entry.getKey(), lang, entry.getValue()))
      .sorted(new HashTagCountComparator().reversed())
      .limit(10)
      .collect(Collectors.toList());

    return top10HashTags;
  }
}