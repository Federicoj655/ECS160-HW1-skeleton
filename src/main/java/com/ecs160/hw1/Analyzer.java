package com.ecs160.hw1;

import java.util.List;

public abstract class Analyzer {
    protected RedisManager redisManager;

    public Analyzer(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    // Expect the list of posts as a parameter
    public abstract double getTotalPosts(List<PostComponent> posts);
    public abstract double getAverageReplies(List<PostComponent> posts);

}
