package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String nickName;
    private String userName;
    private Long createdAt;
    private Long updatedAt;

    public User(String email, String nickName, String userName) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.nickName = nickName;
        this.userName = userName;

        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUserName() {
        return userName;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
    }

    public void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }
}
