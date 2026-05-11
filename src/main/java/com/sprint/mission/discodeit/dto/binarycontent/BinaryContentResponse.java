package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    Instant createdAt,
    String fileName,
    Integer size,
    String contentType,
    byte[] bytes
) {

    public static BinaryContentResponse from(BinaryContent file) {
        return new BinaryContentResponse(
            file.getId(),
            file.getCreatedAt(),
            file.getFileName(),
            file.getData().length,
            file.getContentType(),
            file.getData()
        );
    }

    public static List<BinaryContentResponse> fromList(List<BinaryContent> files) {
        return files.stream()
            .map(BinaryContentResponse::from)
            .toList();
    }

}
