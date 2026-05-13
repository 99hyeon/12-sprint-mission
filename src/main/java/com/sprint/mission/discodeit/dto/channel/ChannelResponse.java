package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.UUID;

public record ChannelResponse(
    UUID id,
    String name,
    ChannelType type
) {
    public static ChannelResponse from(Channel channel){
        return new ChannelResponse(
            channel.getId(),
            channel.getName(),
            channel.getType()
        );
    }
}
