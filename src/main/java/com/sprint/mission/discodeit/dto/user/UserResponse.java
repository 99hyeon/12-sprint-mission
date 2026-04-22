package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String email,
    String nickName,
    String userName,
    UUID profileImageId,
    boolean isOnline
) { }
