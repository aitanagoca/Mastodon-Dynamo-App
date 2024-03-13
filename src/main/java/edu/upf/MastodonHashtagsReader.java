package edu.upf;

import java.util.List;

import edu.upf.model.HashTagCount;
import edu.upf.storage.DynamoHashTagRepository;

public class MastodonHashtagsReader {
    
    public static void main(String[] args) {
        // Validate and retrieve language from command line arguments
        if (args.length != 1) {
            System.err.println("Usage: MastodonHashtagsReader <language>");
            System.exit(1);
        }
        String language = args[0];
        
        // Instantiate DynamoHashTagRepository
        DynamoHashTagRepository repository = new DynamoHashTagRepository();
        
        // Retrieve top 10 hashtags for the specified language
        List<HashTagCount> top10 = repository.readTop10(language.toLowerCase());
        
        // Print top 10 hashtags
        System.out.println("###############################");
        System.out.println("TOP 10 HASHTAGS IN - " + language.toUpperCase() + ":");
        System.out.println("###############################");
        for (int i = 0; i < Math.min(10, top10.size()); i++) {
            HashTagCount tagCount = top10.get(i);
            System.out.println(tagCount.getHashTag() + ": " + tagCount.getCount());
        }
        System.out.println("###############################");
    }
}