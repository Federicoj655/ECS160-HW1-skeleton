package com.ecs160.hw1;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
//idk if needed yet...

import java.util.List;

public class SimpleAnalyzer extends Analyzer {
    public SimpleAnalyzer(RedisManager redisManager) {
        super(redisManager);
    }


    @Override
    public double getTotalPosts(List<PostComponent> posts) {
        return posts.size();
    }

    @Override
    public double getAverageReplies(List<PostComponent> posts) {
        if (posts.isEmpty()) return 0.0;

        int totalReplies = 0;
        int totalPostsWithReplies = 0;  // Only count posts w/ replies

        for (PostComponent post : posts) {
            System.out.println("DEBUG: Post ID: " + post.getId() + " - Replies: " + post.getReplies());

            int replyCount = post.getReplies().size();
            if (replyCount > 0) {
                totalPostsWithReplies++;
                totalReplies += replyCount;
                System.out.println("Post ID: " + post.getId() + " has " + replyCount + " replies.");
            } else {
            }
        }

        System.out.println(" Total Replies Counted: " + totalReplies);
        System.out.println(" Total Posts With Replies: " + totalPostsWithReplies);


        // if no posts have replies, it returns 0.0 to prevent error /0;
        // otherwise, it calculates the average number of replies per post using floating-point division.
        return totalPostsWithReplies == 0 ? 0.0 : (double) totalReplies / totalPostsWithReplies;
    }

}




