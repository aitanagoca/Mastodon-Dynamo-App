package edu.upf;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.github.tukaaa.MastodonDStream;
import com.github.tukaaa.config.AppConfig;
import com.github.tukaaa.model.SimplifiedTweetWithHashtags;

import scala.Tuple2;
import java.util.List;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function2;

public class MastodonWithState {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("Real-time Mastodon With State");
        AppConfig appConfig = AppConfig.getConfig();

        StreamingContext sc = new StreamingContext(conf, Durations.seconds(10));
        JavaStreamingContext jsc = new JavaStreamingContext(sc);
        jsc.checkpoint("/tmp/checkpoint");

        JavaDStream<SimplifiedTweetWithHashtags> stream = new MastodonDStream(sc, appConfig).asJStream();

        // TODO IMPLEMENT ME

        // Filter tweets by language and count tweets per user
        String targetLanguage = args[0].toLowerCase(); 
        final JavaPairDStream<String, Integer> tweetsPerUser = stream
                .filter(tweet -> targetLanguage.equals(tweet.getLanguage()))
                .mapToPair(tweet -> new Tuple2<>(tweet.getUserName(), 1))
                .reduceByKey((count1, count2) -> count1 + count2);

        // Define the update function
        Function2<List<Integer>, Optional<Integer>, Optional<Integer>> updateFunction =
            new Function2<List<Integer>, Optional<Integer>, Optional<Integer>>() {
                @Override
                public Optional<Integer> call(List<Integer> values, Optional<Integer> state) {
                    Integer newSum = state.or(0);
                    for (Integer value : values) {
                        newSum += value;
                    }
                    return Optional.of(newSum);
                }
            };

        // Use updateStateByKey to maintain a running count and swap the key-value pairs for sorting
        JavaPairDStream<Integer, String> updatedCounts = tweetsPerUser.updateStateByKey(updateFunction)
                                                                      .mapToPair(Tuple2::swap)
                                                                      .transformToPair(counts -> counts.sortByKey(false));

        // Print the top 20 users
        updatedCounts.print(20);

        // Start the application and wait for termination signal
        jsc.start();
        jsc.awaitTermination();
    }
}
