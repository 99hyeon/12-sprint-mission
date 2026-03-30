package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private boolean isPublic;
    private String notiTitle;
    private String notiContents;
    private List<Message> messages;
    private List<User> users;
    private Long createdAt;
    private Long updatedAt;

    public Channel(String name, boolean isPublic, String notiTitle, String notiContents) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.isPublic = isPublic;
        this.notiTitle = notiTitle;
        this.notiContents = notiContents;
        this.messages = new ArrayList<>();
        this.users = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public String getNotiTitle() {
        return notiTitle;
    }

    public String getNotiContents() {
        return notiContents;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateName(String name) {
        this.name = name;
        updateUpdatedAt();
    }

    public void updateIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
        updateUpdatedAt();
    }

    public void updateNotiTitle(String notiTitle) {
        this.notiTitle = notiTitle;
        updateUpdatedAt();
    }

    public void updateNotiContents(String notiContents) {
        this.notiContents = notiContents;
        updateUpdatedAt();
    }

    public void updateMessages(Message message) {
        this.messages.add(message);
        updateUpdatedAt();
    }

    public void updateUsers(List<User> users) {
        this.users = users;
        updateUpdatedAt();
    }

    private void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }
}
