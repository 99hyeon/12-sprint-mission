package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponse(
    UUID id,
    String name,
    ChannelType type,
    Instant recentMessageCreatedAt,
    List<UUID> userIds
) {
    public static ChannelFindResponse from(Channel channel, Message recentMessage, List<UUID> userIds){
        Instant messageCreatedAt = recentMessage == null ? null : recentMessage.getUpdatedAt();

        return new ChannelFindResponse(
            channel.getId(),
            channel.getName(),
            channel.getType(),
            messageCreatedAt,
            userIds
        );
    }
}
