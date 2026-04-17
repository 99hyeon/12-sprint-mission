package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends BaseEntity {

    private UUID userId;
    private Instant updatedAt;

    public UserStatus(UUID userId){
        super();
        this.userId = userId;
        this.updatedAt = Instant.now();
    }

    public boolean isOnline(){
        return Duration.between(this.updatedAt, Instant.now()).toMinutes() < 5;
    }

    public void updateUpdatedAt(Instant newLastActiveAt){
        this.updatedAt = newLastActiveAt;
    }

    public void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
