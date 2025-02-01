package com.ecs160.hw1;

import java.util.ArrayList;
import java.util.List;

public class Post implements PostComponent {
    private String id;
    private String content;
    private List<PostComponent> replies;

    public Post(String id, String content) {
        this.id = id;
        this.content = content;
        this.replies = new ArrayList<>(); // Initialize empty list
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public List<PostComponent> getReplies() {
        return replies;
    }

    // Method to add reply to A Posd
    public void addReply(PostComponent reply) {
        replies.add(reply);
    }

    public void setReplies(List<PostComponent> replies) {
        this.replies = replies;
    }


}
