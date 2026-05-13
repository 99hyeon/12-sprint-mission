package com.sprint.mission.discodeit.dto.channel;

public record ChannelUpdateRequest(
    String name,
    String notiTitle,
    String notiContents
) {}
