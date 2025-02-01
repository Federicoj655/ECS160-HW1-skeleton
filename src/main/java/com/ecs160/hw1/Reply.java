package com.ecs160.hw1;

public class Reply extends Post {
    private String parentId; // Add parentId field

    public Reply(String id, String content, String parentId) {
        super(id, content);
        this.parentId = parentId; // Store parent ID
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
