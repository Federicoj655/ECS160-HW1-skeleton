package com.ecs160.hw1;

import redis.clients.jedis.Jedis;
import com.google.gson.Gson;
import java.util.*;

public class RedisManager {
    private Jedis jedis;
    private Gson gson;

    public RedisManager() {
        this.jedis = new Jedis("localhost", 6379); // Default Redis Port
        this.gson = new Gson();
        System.out.println(" Connected to Redis successfully!");
    }

    public void storePost(Post post) {
        String postKey = "post:" + post.getId();
        jedis.hset(postKey, "id", post.getId());
        jedis.hset(postKey, "content", post.getContent());

        List<PostComponent> replies = post.getReplies();
        for (PostComponent reply : replies) {
            jedis.rpush(postKey + ":replies", reply.getId());
            storeReply((Reply) reply);
        }

        System.out.println("📌 Stored Post: " + post.getId());
    }

    public void storeReply(Reply reply) {
        String replyKey = "reply:" + reply.getId();
        jedis.hset(replyKey, "id", reply.getId());
        jedis.hset(replyKey, "content", reply.getContent());
        jedis.hset(replyKey, "parentId", reply.getParentId());

        for (PostComponent nestedReply : reply.getReplies()) {
            jedis.rpush(replyKey + ":replies", nestedReply.getId());
            storeReply((Reply) nestedReply); // store replies
        }

        System.out.println(" Stored Reply: " + reply.getId() + " (Parent: " + reply.getParentId() + ")");
    }

    public Post getPost(String postId) {
        String postKey = "post:" + postId;
        if (!jedis.exists(postKey)) return null;

        String id = jedis.hget(postKey, "id");
        String content = jedis.hget(postKey, "content");
        Post post = new Post(id, content);

        // Get replies
        List<String> replyIds = jedis.lrange(postKey + ":replies", 0, -1);
        for (String replyId : replyIds) {
            Reply reply = getReply(replyId);
            if (reply != null) {
                post.addReply(reply);
                System.out.println("Attached Reply: " + reply.getId() + " to Post: " + post.getId());
            }
        }

        return post;
    }
    public Reply getReply(String replyId) {
        String replyKey = "reply:" + replyId;
        if (!jedis.exists(replyKey)) return null;

        String id = jedis.hget(replyKey, "id");
        String content = jedis.hget(replyKey, "content");
        String parentId = jedis.hget(replyKey, "parentId");

        Reply reply = new Reply(id, content, parentId);

        // Get nested replies
        List<String> nestedReplyIds = jedis.lrange(replyKey + ":replies", 0, -1);
        for (String nestedReplyId : nestedReplyIds) {
            Reply nestedReply = getReply(nestedReplyId);
            if (nestedReply != null) {
                reply.addReply(nestedReply);
                System.out.println("🔗 Attached Nested Reply: " + nestedReply.getId() + " to Parent Reply: " + reply.getId());
            }
        }

        return reply;
    }
    // Close connection
    public void close() {
        jedis.close();
    }
}
