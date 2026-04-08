package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FileStore;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final UserService userService;
    private final ChannelService channelService;

    private final File file;
    private final FileStore<Map<UUID, Message>> fileStore;

    public FileMessageService(String filePath, UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        this.file = new File(filePath);
        this.fileStore = new FileStore<>(filePath, "Message");
    }


    @Override
    public Message create(Message message) {
        checkValidation(message);

        Map<UUID, Message> data = loadOrEmpty();
        data.put(message.getId(), message);
        fileStore.save(data);
        return message;
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(loadOrEmpty().get(id));
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(loadOrEmpty().values());
    }

    @Override
    public Message update(Message message) {
        checkValidation(message);

        Map<UUID, Message> data = loadOrEmpty();
        if (!data.containsKey(message.getId())) {
            throw new IllegalArgumentException("존재하지 않는 메세지");
        }
        data.put(message.getId(), message);
        fileStore.save(data);
        return message;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> data = loadOrEmpty();
        data.remove(id);
        fileStore.save(data);
    }

    private Map<UUID, Message> loadOrEmpty() {
        Map<UUID, Message> data = fileStore.load();
        return data == null ? new HashMap<>() : data;
    }

    private void checkValidation(Message message) {
        userService.read(message.getUser().getId()).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 사용자")
        );
        channelService.read(message.getChannel().getId()).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 채널")
        );
    }
}
