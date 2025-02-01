package com.ecs160.hw1;

import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) {
        //  Command line Interface
        Options options = new Options();
        options.addOption("file", true, "Path to input JSON file");
        options.addOption("weighted", true, "Use weighted analysis (true/false)");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            // Extract
            String filePath = cmd.getOptionValue("file", "src/main/resources/input.json"); // Default path if not provided
            boolean weighted = Boolean.parseBoolean(cmd.getOptionValue("weighted", "false"));

            System.out.println(" DEBUG: File Path = " + filePath);
            System.out.println(" DEBUG: Weighted Analysis = " + weighted);


            JsonParserUtil jsonParser = new JsonParserUtil();
            var posts = jsonParser.parseJson(filePath);

            Analyzer analyzer;
            RedisManager redisManager = new RedisManager();
            if (weighted) {
                analyzer = new WeightedAnalyzer(redisManager);
            } else {
                analyzer = new SimpleAnalyzer(redisManager);
            }

            // Analysis
            double totalPosts = analyzer.getTotalPosts(posts);
            double avgReplies = analyzer.getAverageReplies(posts);

            System.out.println("\n Analysis Results:");
            System.out.println(" Total Posts: " + totalPosts);
            System.out.println(" Average Replies per Post: " + avgReplies);

        } catch (ParseException e) {
            System.err.println(" Error parsing command-line arguments: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar HW1-solution-1.0-SNAPSHOT.jar", options);
        } catch (Exception e) {
            System.err.println(" error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
