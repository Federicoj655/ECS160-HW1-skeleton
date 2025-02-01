package com.ecs160.hw1;

import java.util.List;

public class Thread implements PostComponent {
    private Post mainPost;

    public Thread(Post mainPost) {
        this.mainPost = mainPost;
    }

    @Override
    public String getId() {
        return mainPost.getId();
    }

    @Override
    public String getContent() {
        return mainPost.getContent();
    }

    @Override
    public List<PostComponent> getReplies() {
        return mainPost.getReplies();
    }

    public void addReply(PostComponent reply) {
        mainPost.addReply(reply);
    }
}
