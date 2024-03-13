package edu.upf;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.github.tukaaa.MastodonDStream;
import com.github.tukaaa.config.AppConfig;
import com.github.tukaaa.model.SimplifiedTweetWithHashtags;

import edu.upf.model.HashTagCount;
import edu.upf.storage.DynamoHashTagRepository;
import edu.upf.storage.IHashtagRepository;

public class MastodonHashtags {

        public static void main(String[] args) throws InterruptedException {
                SparkConf conf = new SparkConf().setAppName("Real-time Mastodon Hashtags");
                AppConfig appConfig = AppConfig.getConfig();
                StreamingContext sc = new StreamingContext(conf, Durations.seconds(10));
                JavaStreamingContext jsc = new JavaStreamingContext(sc);
                jsc.checkpoint("/tmp/checkpoint");

                JavaDStream<SimplifiedTweetWithHashtags> stream = new MastodonDStream(sc, appConfig).asJStream();

                // TODO IMPLEMENT ME

                // Setting the target language from command line arguments
                String targetLanguage = args[0].toLowerCase();

                // Processing each RDD in the DStream
                stream.foreachRDD(rdd -> {
                        // Processing each record in the RDD
                        rdd.foreach(record -> {
                                // Writing the record to DynamoDB
                                DynamoHashTagRepository repository = new DynamoHashTagRepository();
                                repository.write(record);
                        });
                    
                        // Retrieve and print the top 10 hashtags
                        DynamoHashTagRepository repository = new DynamoHashTagRepository();
                        List<HashTagCount> top10 = repository.readTop10(targetLanguage);
                        System.out.println("###############################");
                        System.out.println("TOP 10 HASHTAGS IN - " + targetLanguage + ":");
                        System.out.println("###############################");
                        for (int i = 0; i < Math.min(10, top10.size()); i++) {
                            HashTagCount tagCount = top10.get(i);
                            System.out.println(tagCount.getHashTag() + ": " + tagCount.getCount());
                        }
                        System.out.println("###############################");
                    });

                // Start the application and wait for termination signal
                jsc.start();
                jsc.awaitTermination();
        }
}