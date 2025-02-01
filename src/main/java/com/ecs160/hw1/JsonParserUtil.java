package com.ecs160.hw1;

import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class JsonParserUtil {

    public static List<PostComponent> parseJson(String filePath) {
        List<PostComponent> posts = new ArrayList<>();
        Map<String, Post> postMap = new HashMap<>(); // Store posts by ID for lookup

        try {
            JsonElement element = JsonParser.parseReader(new FileReader(filePath));

            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                JsonArray feedArray = jsonObject.getAsJsonArray("feed");

                for (JsonElement feedElement : feedArray) {
                    JsonObject feedObject = feedElement.getAsJsonObject();

                    if (!feedObject.has("thread")) {
                        System.out.println("️ Skipping entry with no 'thread' object: " + feedObject);
                        continue;
                    }

                    JsonObject threadObject = feedObject.getAsJsonObject("thread");

                    if (threadObject.has("post")) {
                        Post mainPost = parsePost(threadObject.getAsJsonObject("post"));

                        if (mainPost != null) {
                            posts.add(mainPost);
                            postMap.put(mainPost.getId(), mainPost);
                        }

                        //  Process replies
                        if (threadObject.has("replies")) {
                            JsonArray repliesArray = threadObject.getAsJsonArray("replies");
                            List<Reply> replies = parseReplies(repliesArray);

                            for (Reply reply : replies) {
                                posts.add(reply);
                                postMap.put(reply.getId(), reply); // Store replies in postMap too

                                if (postMap.containsKey(reply.getParentId())) {
                                    postMap.get(reply.getParentId()).addReply(reply);
                                    System.out.println(" reply1 " + reply.getId() + " to parent " + reply.getParentId());
                                } else {
                                    System.out.println("Orphan : " + reply.getId());
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(" Error: Failed to parse the JSON file: " + e.getMessage());
        }

        return posts;
    }




    public static Post parsePost(JsonObject jsonPost) {
        try {
            String id = jsonPost.has("uri") ? jsonPost.get("uri").getAsString() : "Unknown";
            JsonObject record = jsonPost.getAsJsonObject("record");
            String content = record.has("text") ? record.get("text").getAsString() : "No Content";

            return new Post(id, content);
        } catch (Exception e) {
            System.out.println(" Error parsing post: " + e.getMessage());
            return null; // Skip invalid posts
        }
    }



    public static List<Reply> parseReplies(JsonArray jsonReplies) {
        List<Reply> replies = new ArrayList<>();
        for (JsonElement elem : jsonReplies) {
            try {
                JsonObject jsonReplyThread = elem.getAsJsonObject();

                if (!jsonReplyThread.has("post")) {
                    System.out.println(" Skipping reply with no 'post' object: " + jsonReplyThread);
                    continue;
                }

                JsonObject jsonReply = jsonReplyThread.getAsJsonObject("post"); // Get nested "post" object
                JsonObject record = jsonReply.getAsJsonObject("record");


                String content = record.has("text") ? record.get("text").getAsString() : "No Content";
                String id = jsonReply.has("uri") ? jsonReply.get("uri").getAsString() : "Unknown";

                String parentId = "Unknown";
                if (record.has("reply")) {
                    JsonObject replyInfo = record.getAsJsonObject("reply");
                    if (replyInfo.has("parent")) {
                        parentId = replyInfo.getAsJsonObject("parent").get("uri").getAsString();
                    }
                }

                Reply reply = new Reply(id, content, parentId);
                replies.add(reply);

                System.out.println(" Parsed Reply: ID = " + id + ", Parent = " + parentId + ", Content = " + content);

            } catch (Exception e) {
                System.out.println(" Error parsing reply: " + e.getMessage());
            }
        }
        return replies;
    }






}
