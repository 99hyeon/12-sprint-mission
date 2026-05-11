package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<UUID> attachmentIds
) {

    public static MessageResponse from(Message message, List<BinaryContent> attachmentIds) {
        return new MessageResponse(
            message.getId(),
            message.getCreatedAt(),
            message.getUpdatedAt(),
            message.getContent(),
            message.getChannelId(),
            message.getUserId(),
            attachmentIds.stream()
                .map(BinaryContent::getId)
                .toList()
        );
    }
}
