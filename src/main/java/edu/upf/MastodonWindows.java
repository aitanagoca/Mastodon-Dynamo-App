package edu.upf;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.github.tukaaa.MastodonDStream;
import com.github.tukaaa.config.AppConfig;
import com.github.tukaaa.model.SimplifiedTweetWithHashtags;

import edu.upf.util.LanguageMapUtils;
import scala.Tuple2;

public class MastodonWindows {
        public static void main(String[] args) {
                String input = args[0];

                SparkConf conf = new SparkConf().setAppName("Real-time Mastodon Stateful with Windows Exercise");
                AppConfig appConfig = AppConfig.getConfig();

                StreamingContext sc = new StreamingContext(conf, Durations.seconds(10));
                JavaStreamingContext jsc = new JavaStreamingContext(sc);
                jsc.checkpoint("/tmp/checkpoint");

                JavaDStream<SimplifiedTweetWithHashtags> stream = new MastodonDStream(sc, appConfig).asJStream();

                // TODO IMPLEMENT ME

                // Read language mapping from the file
		final JavaRDD<String> languageLines = jsc.sparkContext().textFile(input);
		
                // // Build language map
                final JavaPairRDD<String, String> languageMap = LanguageMapUtils.buildLanguageMap(languageLines)
                                                                            .reduceByKey((a, b) -> a);

                // Compute language ranking for each micro-batch (20 seconds)
                final JavaPairDStream<Integer, String> languageRankStream = stream
                        .mapToPair(tweet -> new Tuple2<String, Integer>(tweet.getLanguage(), 1))
                        .reduceByKey((count1, count2) -> count1 + count2)
                        .transformToPair(rdd -> rdd.join(languageMap)
                                                   .mapToPair(element -> new Tuple2<Integer, String>(element._2._1, element._2._2 + " <-- MICRO BATCH"))
                                                   .sortByKey(false)
                                                   );

                // Compute language ranking for each window (60 seconds)
                final JavaPairDStream<Integer, String> languageWindow = stream
                        .mapToPair(tweet -> new Tuple2<String, Integer>(tweet.getLanguage(), 1))
                        .reduceByKeyAndWindow((count1, count2) -> count1 + count2, Durations.seconds(60))
                        .transformToPair(rdd -> rdd.join(languageMap)
                                                .mapToPair(element -> new Tuple2<Integer, String>(element._2._1, element._2._2 + " <-- WINDOW"))
                                                .sortByKey(false)
                                                );
                
                // Print top 15 languages from the stream and window
                languageRankStream.print(15);
                languageWindow.print(15);

                ///////

                // Start the application and wait for termination signal
                sc.start();
                sc.awaitTermination();
        }

}
