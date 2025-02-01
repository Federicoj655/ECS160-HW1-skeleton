package com.ecs160.hw1;

import org.apache.commons.cli.*;
import java.util.List;

public class SocialMediaAnalyzerDriver {
    public static void main(String[] args) {
        // Create CLI Options
        Options options = new Options();
        options.addOption("file", true, "Path to input JSON file");
        options.addOption("weighted", true, "Use weighted analysis (true/false)");

        // Parse command-line arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        String filePath = "src/main/resources/input.json"; // Default file
        boolean weightedAnalysis = false; // Default to simple analysis

        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("file")) {
                filePath = cmd.getOptionValue("file");
            }

            if (cmd.hasOption("weighted")) {
                weightedAnalysis = Boolean.parseBoolean(cmd.getOptionValue("weighted"));
            }
        } catch (ParseException e) {
            System.err.println("Error parsing command-line arguments: " + e.getMessage());
            return;
        }

        System.out.println("\n📂 Parsing JSON from: " + filePath);
        List<PostComponent> posts = JsonParserUtil.parseJson(filePath);

        RedisManager redisManager = new RedisManager();
        for (PostComponent post : posts) {
            if (post instanceof Post) {
                redisManager.storePost((Post) post);
            }
        }
        System.out.println("✅ Stored posts and replies in Redis.");

        Analyzer analyzer = weightedAnalysis ? new WeightedAnalyzer(redisManager) : new SimpleAnalyzer(redisManager);
        System.out.println("\n🔍 Running " + (weightedAnalysis ? "Weighted" : "Simple") + " Analysis...");

        double totalPosts = analyzer.getTotalPosts(posts);
        double avgReplies = analyzer.getAverageReplies(posts);
        //String avgInterval = analyzer.getAverageInterval(posts);
        // do not know how to implement the interval time on json

        System.out.println("\n📊 Analysis Results:");
        System.out.println("🔹 Total Posts: " + totalPosts);
        System.out.println("🔹 Average Replies per Post: " + avgReplies);
        //System.out.println("🔹 Average Interval Between Comments: " + avgInterval);
    }
}
