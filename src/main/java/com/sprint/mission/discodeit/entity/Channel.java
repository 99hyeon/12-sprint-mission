package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity {

    private String name;
    private ChannelType type;
    private String notiTitle;
    private String notiContents;
    private List<UUID> messages;
    private List<UUID> users;
    private Instant updatedAt;

    public Channel(String name, ChannelType type, String notiTitle, String notiContents) {
        super();
        this.name = name;
        this.type = type;
        this.notiTitle = notiTitle;
        this.notiContents = notiContents;
        this.messages = new ArrayList<>();
        this.users = new ArrayList<>();
        this.updatedAt = Instant.now();
    }

    public void updateName(String name) {
        this.name = name;
        updateUpdatedAt();
    }

    public void updateChannelType(ChannelType type) {
        this.type = type;
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

    public void addMessage(UUID messageId) {
        this.messages.add(messageId);
        updateUpdatedAt();
    }

    public void addUser(UUID userId) {
        if (!this.users.contains(userId)) {
            this.users.add(userId);
            updateUpdatedAt();
        }
    }

    public void addUsers(List<UUID> userIds) {
        boolean changed = false;

        for (UUID userId : userIds) {
            if (!this.users.contains(userId)) {
                this.users.add(userId);
                changed = true;
            }
        }

        if (changed) {
            updateUpdatedAt();
        }
    }

    public void changeChannel(String name, String notiTitle, String notiContents){
        if(name != null){
            this.name = name;
        }
        if(notiTitle != null){
            this.notiTitle = notiTitle;
        }
        if(notiContents != null){
            this.notiContents = notiContents;
        }
        updateUpdatedAt();
    }

    private void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
