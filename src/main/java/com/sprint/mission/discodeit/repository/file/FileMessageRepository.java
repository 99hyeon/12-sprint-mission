package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FileStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private final FileStore<Map<UUID, Message>> fileStore;

    public FileMessageRepository(String filePath) {
        this.fileStore = new FileStore<>(filePath, "Message");
    }


    @Override
    public Message save(Message message) {
        Map<UUID, Message> data = loadOrEmpty();
        data.put(message.getId(), message);
        fileStore.save(data);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(loadOrEmpty().get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(loadOrEmpty().values());
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
}
