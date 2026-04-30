package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId,
    Boolean online
) {
    public static UserDto from(User user, UserStatus userStatus){
        return new UserDto(
            user.getId(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getUserName(),
            user.getEmail(),
            user.getProfileImageId(),
            userStatus.isOnline()
        );
    }
}
