package com.sprint.mission.discodeit.entity;

import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent extends BaseEntity {

    private String fileName;
    private String contentType;
    private byte[] data;
    private UUID userId;
    private UUID messageId;

    public BinaryContent(String fileName, String contentType, byte[] data, UUID userId,
        UUID messageId) {
        super();
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        this.userId = userId;
        this.messageId = messageId;
    }

}
