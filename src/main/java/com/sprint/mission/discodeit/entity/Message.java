package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Channel channel;
    private User user;
    private String content;
    private Long createdAt;
    private Long updatedAt;

    public Message(Channel channel, User user, String content) {
        this.channel = channel;
        this.user = user;
        this.content = content;
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public Channel getChannel(){
        return channel;
    }

    public User getUser(){
        return user;
    }

    public String getContent(){
        return content;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateChannel(Channel channel){
        this.channel = channel;
        updateUpdatedAt();
    }

    public void updateUser(User user){
        this.user = user;
        updateUpdatedAt();
    }

    public void updateContent(String content){
        this.content = content;
        updateUpdatedAt();
    }

    private void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }
}
