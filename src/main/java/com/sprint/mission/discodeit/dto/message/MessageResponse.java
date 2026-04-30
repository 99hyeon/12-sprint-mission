package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    String content,
    UUID channelId,
    UUID userId,
    List<BinaryContentResponse> files
) {
    public static MessageResponse from(Message message, List<BinaryContent> files){
        return new MessageResponse(
            message.getId(),
            message.getContent(),
            message.getChannelId(),
            message.getUserId(),
            BinaryContentResponse.fromList(files)
        );
    }
}
