package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponse(
    UUID id,
    String name,
    ChannelType type,
    Instant recentMessageCreatedAt,
    List<UUID> userIds
) {
    public static ChannelFindResponse from(Channel channel, Message recentMessage){
        Instant messageCreatedAt = recentMessage == null ? null : recentMessage.getUpdatedAt();

        List<UUID> users = new ArrayList<>();
        if (channel.getType() == ChannelType.PRIVATE) {
            users = new ArrayList<>(channel.getUsers());
        }

        return new ChannelFindResponse(
            channel.getId(),
            channel.getName(),
            channel.getType(),
            messageCreatedAt,
            users
        );
    }
}
