package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends UpdatableBaseEntity {

    private static final long ONLINE_THRESHOLD_MINUTES = 5;

    private UUID userId;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
    }

    public boolean isOnline() {
        return Duration.between(this.updatedAt, Instant.now()).toMinutes()
            < ONLINE_THRESHOLD_MINUTES;
    }

    public void updateUpdatedAt(Instant newLastActiveAt) {
        this.updatedAt = newLastActiveAt;
    }
}
