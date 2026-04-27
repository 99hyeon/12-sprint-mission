package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus extends BaseEntity {

    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;
    private Instant updatedAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
        this.updatedAt = Instant.now();
    }

    public void changeLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public void changeReadStatus(Instant newLastReadAt) {
        if (newLastReadAt != null) {
            this.lastReadAt = newLastReadAt;
        }
        updateUpdatedAt();
    }

    private void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
