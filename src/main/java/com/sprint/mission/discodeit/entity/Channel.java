package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import java.util.UUID;

@Getter
public class Channel extends UpdatableBaseEntity {

    private String name;
    private ChannelType type;
    private String description;
    private List<UUID> messages;

    private Channel(String name, ChannelType type, String description) {
        super();
        this.name = name;
        this.type = type;
        this.description = description;
        this.messages = new ArrayList<>();
    }

    public static Channel createPublic(String name, String description) {
        return new Channel(name, ChannelType.PUBLIC, description);
    }

    public static Channel createPrivate(String name) {
        return new Channel(name, ChannelType.PRIVATE, null);
    }

    public void updateName(String name) {
        this.name = name;
        updateUpdatedAt();
    }

    public void updateChannelType(ChannelType type) {
        this.type = type;
        updateUpdatedAt();
    }

    public void updateDescription(String description) {
        this.description = description;
        updateUpdatedAt();
    }

    public void addMessage(UUID messageId) {
        this.messages.add(messageId);
        updateUpdatedAt();
    }

    public void changeChannel(String name, String description) {
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        updateUpdatedAt();
    }

}
