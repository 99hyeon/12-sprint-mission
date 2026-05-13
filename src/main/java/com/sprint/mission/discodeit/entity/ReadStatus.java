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

    public ReadStatus(UUID userId, UUID channelId){
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = null;
        this.updatedAt = Instant.now();
    }

    public void changeLastReadAt(Instant lastReadAt){
        this.lastReadAt = lastReadAt;
    }

    public void changeReadStatus(UUID userId, UUID channelId, Instant lastReadAt){
        if(userId != null){
            this.userId = userId;
        }
        if(channelId != null){
            this.channelId = channelId;
        }
        if(lastReadAt != null){
            this.lastReadAt = lastReadAt;
        }
        updateUpdatedAt();
    }

    private void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
