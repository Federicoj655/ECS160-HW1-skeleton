package com.ecs160.hw1;

import java.util.List;

public class WeightedAnalyzer extends Analyzer {

    public WeightedAnalyzer(RedisManager redisManager) {
        super(redisManager);
    }

    @Override
    public double getTotalPosts(List<PostComponent> posts) {
        System.out.println("DEBUG: Entered getTotalPosts() function"); // <-- Print immediately

        if (posts == null || posts.isEmpty()) {
            System.out.println("DEBUG: No posts available.");
            return 0.0;
        }

        double totalWeightedPosts = 0;
        int longestPost = getLongestPostLength(posts);

        for (PostComponent post : posts) {
            System.out.println("DEBUG: Longest Post Length: " + longestPost);
            if (!(post instanceof Reply)) {  // Only count top-level posts to avoid overcounting...
                int wordCount = post.getContent().split("\\s+").length;
                double postWeight = 1 + ((double) wordCount / longestPost);//i dont know why but the weight is not working:: it was due to analyzer interface error
                // i trued debugging multiple times i cant figure out why.
                totalWeightedPosts += postWeight;

                System.out.println("DEBUG: Counting Post ID: " + post.getId() + ", Weight: " + postWeight);
            }
        }

        System.out.println("DEBUG: Final Weighted Total Posts: " + totalWeightedPosts);
        return totalWeightedPosts;
    }

    private int getLongestPostLength(List<PostComponent> posts) {
        int maxLength = 0;
        for (PostComponent post : posts) {
            int wordCount = post.getContent().split("\\s+").length;
            if (wordCount > maxLength) {
                maxLength = wordCount;
            }
        }
        return maxLength;
    }


    @Override
    public double getAverageReplies(List<PostComponent> posts) {
        System.out.println("DEBUG: Entered getAverageReplies() function"); // <-- Ensure function is called

        if (posts.isEmpty()) return 0.0;

        int longestPost = getLongestPostLength(posts);
        double totalWeightedReplies = 0.0;
        int totalReplyingPosts = 0; // Only count posts that actually have replies

        for (PostComponent post : posts) {
            System.out.println("DEBUG: Checking Post ID: " + post.getId()); // <-- Force debug

            if (!post.getReplies().isEmpty()) {
                totalReplyingPosts++;
                for (PostComponent reply : post.getReplies()) {
                    int replyWordCount = reply.getContent().split("\\s+").length;
                    double replyWeight = 1.0 + ((double) replyWordCount / longestPost);
                    totalWeightedReplies += replyWeight;

                    System.out.println("DEBUG: Post ID: " + post.getId() + " - Replies: " + post.getReplies());
                    System.out.println("DEBUG: Checking Post ID: " + post.getId() + ", Replies: " + post.getReplies().size());

                }
            }
        }

        System.out.println("DEBUG: Total Weighted Replies: " + totalWeightedReplies);
        System.out.println("DEBUG: Total Replying Posts: " + totalReplyingPosts);


        return totalReplyingPosts == 0 ? 0 : totalWeightedReplies / totalReplyingPosts;
    }

}

