package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import lombok.Getter;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {

    private String content;
    private UUID channelId;
    private UUID userId;
    private Instant updatedAt;

    public Message(String content, UUID channelId, UUID userId) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.userId = userId;
        this.updatedAt = Instant.now();
    }

    public void changeContent(String content){
        this.content = content;
        updateUpdatedAt();
    }

    public void updateChannelId(UUID channelId){
        this.channelId = channelId;
        updateUpdatedAt();
    }

    public void updateUserId(UUID userId){
        this.userId = userId;
        updateUpdatedAt();
    }

    private void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
