package com.ecs160.hw1;

import java.util.List;

// The interface that both Post and Reply will implement/extend idk quite yet::reply extends post, post implements PostComponent
public interface PostComponent {
    String getId();

    String getContent();

    List<PostComponent> getReplies();
}