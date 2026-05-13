package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String userName,
    String email,
    String password,
    UUID profileImageId
) {
    public static UserResponse from(User user){
        return new UserResponse(
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
