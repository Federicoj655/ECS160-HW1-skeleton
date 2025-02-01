package com.ecs160.hw1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerTest {

    @Test
    public void testSimpleAnalyzer() {
        List<PostComponent> posts = new ArrayList<>();
        Post post1 = new Post("1", "Hello world");
        Post post2 = new Post("2", "Another post");
        Post post3 = new Post("3", "A third post for completeness");

        Reply reply1 = new Reply("4", "Reply to first post", "1");
        post1.addReply(reply1);

        posts.add(post1);
        posts.add(post2);
        posts.add(post3);

        System.out.println("DEBUG: Reached before assertions");

        Analyzer analyzer = new SimpleAnalyzer(new RedisManager());
        double totalPosts = analyzer.getTotalPosts(posts);
        System.out.println("🔎 DEBUG: SimpleAnalyzer - Total Posts Computed = " + totalPosts);

        assertEquals(3.0, totalPosts, 0.001); // Allow floating point precision
    }

    @Test
    public void testWeightedAnalyzer() {
        List<PostComponent> posts = new ArrayList<>();
        Post post1 = new Post("1", "Hello world");
        Post post2 = new Post("2", "Another post");
        Post post3 = new Post("3", "A third post for completeness");

        Reply reply1 = new Reply("4", "This reply contains multiple words for weight testing.", "1");
        post1.addReply(reply1);
        Reply reply2 = new Reply("5", "A second reply to test weighted analysis.", "1");
        post1.addReply(reply2);

        posts.add(post1);
        posts.add(post2);
        posts.add(post3);

        System.out.println(" DEBUG: Posts list size before calling getTotalPosts() = " + posts.size());
        for (PostComponent post : posts) {
            System.out.println(" Post ID: " + post.getId() + " | Content: " + post.getContent());
            System.out.println(" Replies Count: " + post.getReplies().size());
        }

        Analyzer analyzer = new WeightedAnalyzer(new RedisManager());
        double weightedTotalPosts = analyzer.getTotalPosts(posts);
        double weightedAvgReplies = analyzer.getAverageReplies(posts);

        System.out.println(" DEBUG: WeightedAnalyzer - Total Weighted Posts Computed = " + weightedTotalPosts);
        System.out.println(" DEBUG: WeightedAnalyzer - Weighted Avg Replies Computed = " + weightedAvgReplies);

        assertTrue(weightedTotalPosts > 0, "Weighted total posts should be greater than 0");
        assertTrue(weightedAvgReplies >= 0, "Weighted average replies should be non-negative");
    }
}
