package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus extends BaseEntity {

    private UUID userId;
    private UUID channelId;
    private Instant updatedAt;

    public ReadStatus(UUID userId, UUID channelId){
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.updatedAt = Instant.now();
    }

    public void updateReadStatus(UUID userId, UUID channelId){
        this.userId = userId;
        this.channelId = channelId;
        updateUpdatedAt();
    }

    private void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
