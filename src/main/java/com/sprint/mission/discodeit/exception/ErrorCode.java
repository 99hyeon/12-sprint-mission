package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("User with id %s not found"),
    USER_EMAIL_NOT_FOUND("User with email %s not found"),
    USER_EMAIL_ALREADY_EXIST("User with email %s already exists"),
    USER_USERNAME_ALREADY_EXIST("User with username %s already exists"),
    USER_ID_AND_MESSAGE_ID_MUST_NOT_BE_NULL("userId와 messageId는 둘 다 null일 수 없습니다."),
    WRONG_PASSWORD("Wrong password"),
    USERSTATUS_WITH_USERID_NOT_FOUND("UserStatus with userId %s not found"),
    USER_DUPLICATE("유저 중복"),

    CHANNEL_NOT_FOUND("Channel with id %s not found"),
    PRIVATE_CHANNEL_CANNOT_UPDATE("Private channel cannot be updated"),
    PRIVATE_CHANNEL_READ_STATUS_FORBIDDEN(
        "Private channel ReadStatus can only be created internally"),

    MESSAGE_NOT_FOUND("Message with id %s not found"),

    USERSTATUS_NOT_FOUND("UserStatus with id %s not found"),
    USERSTATUS_ALREADY_EXIST("UserStatus with id %s already exists"),

    BINARYCONTENT_NOT_FOUND("BinaryContent with id %s not found"),

    READSTATUS_NOT_FOUND("ReadStatus with id %s not found"),
    READSTATUS_ALREADY_EXIST("ReadStatus with userId %s and channelId %s already exists"),

    FILE_PROCESSING_ERROR("파일 처리 중 오류 발생");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return message.formatted(args);
    }
}
