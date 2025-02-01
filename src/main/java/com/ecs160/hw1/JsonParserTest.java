package com.ecs160.hw1;

import java.util.List;

public class JsonParserTest {
    public static void main(String[] args) {
        //absoolute path seems like it would not work with the grade
        String filePath = "src/main/resources/input.json";
        List<PostComponent> posts = JsonParserUtil.parseJson(filePath);

        System.out.println(" Total posts parsed: " + posts.size());
        for (PostComponent post : posts) {
            //for post in posts get id and content
            System.out.println(" Post ID: " + post.getId() + " | Content: " + post.getContent());

            if (!post.getReplies().isEmpty()) {
                System.out.println("   Replies:");
                //check replies
                for (PostComponent reply : post.getReplies()) {
                    System.out.println("      Reply ID: " + reply.getId() + " | Content: " + reply.getContent());
                }
            }
        }
    }
}

