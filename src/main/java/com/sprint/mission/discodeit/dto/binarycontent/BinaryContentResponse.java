package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    String fileName,
    String contentType,
    byte[] data,
    UUID userId,
    UUID messageId
) {
    public static BinaryContentResponse from(BinaryContent file) {
        return new BinaryContentResponse(
            file.getId(),
            file.getFileName(),
            file.getContentType(),
            file.getData(),
            file.getUserId(),
            file.getMessageId()
        );
    }

    public static List<BinaryContentResponse> fromList(List<BinaryContent> files) {
        return files.stream()
            .map(BinaryContentResponse::from)
            .toList();
    }

}
