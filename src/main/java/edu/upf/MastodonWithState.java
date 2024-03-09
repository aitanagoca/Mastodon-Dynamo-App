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
        final JavaPairDStream<Integer, String> tweetsPerUser = stream
                .filter(tweet -> targetLanguage.equals(tweet.getLanguage()))
                .mapToPair(tweet -> new Tuple2<>(tweet.getUserName(), 1))
                .reduceByKey((count1, count2) -> count1 + count2)
                .mapToPair(Tuple2::swap)
                .transformToPair(counts -> counts.sortByKey(false));

        // Print top 20 users by tweet count
        tweetsPerUser.print(20);

        ///////

        // Start the application and wait for termination signal
        jsc.start();
        jsc.awaitTermination();
    }

}