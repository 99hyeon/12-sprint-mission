package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponse(
    UUID id,
    String name,
    ChannelType type,
    Instant recentMessageCreatedAt,
    List<UUID> userIds
) {}
