package com.sprint.mission.discodeit.dto.userstatus;

import java.util.UUID;

public record UserStatusResponse(
    UUID id,
    UUID userId,
    boolean isOnline
) {}
