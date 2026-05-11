package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import lombok.Getter;

@Getter
public class UpdatableBaseEntity extends BaseEntity {

    protected Instant updatedAt;

    protected UpdatableBaseEntity() {
        super();
        this.updatedAt = this.createdAt;
    }

    protected void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }

}
