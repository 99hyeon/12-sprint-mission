package com.sprint.mission.discodeit.dto.readstatus;

import java.util.UUID;

public record ReadStatusResponse(
    UUID id,
    UUID userId,
    UUID channelId
) {}
