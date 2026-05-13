package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public record LoginResponse(
    UUID userId,
    String email,
    String userName
) {
    public static LoginResponse from(User user) {
        return new LoginResponse(
            user.getId(),
            user.getEmail(),
            user.getUserName()
        );
    }
}
