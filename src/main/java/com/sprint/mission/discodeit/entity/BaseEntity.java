package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    protected UUID id;
    protected Instant createdAt;

    // updatedAt은 모든 엔티티에 공통으로 필요한 값이 아니어서 BaseEntity에는 두지 않았습니다.
    // BinaryContent 같은 불변 객체는 updatedAt이 불필요하고
    // 수정 가능한 엔티티만 각 엔티티에서 updatedAt을 별도로 관리했습니다.
    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }
}
