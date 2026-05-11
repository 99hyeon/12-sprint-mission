package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record LoginResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    UUID profileId
) {

    public static LoginResponse from(User user) {
        return new LoginResponse(
            user.getId(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getUserName(),
            user.getEmail(),
            user.getPassword(),
            user.getProfileImageId()
        );
    }
}
