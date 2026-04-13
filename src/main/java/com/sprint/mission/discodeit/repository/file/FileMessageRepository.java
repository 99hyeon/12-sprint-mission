package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FileStore;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
    prefix = "discodeit.repository",
    name = "type",
    havingValue = "file"
)
public class FileMessageRepository implements MessageRepository {
    private static final String TARGET_NAME = "Message";
    private static final String FILE_PATH = "data/messages.ser";


    private final FileStore<Map<UUID, Message>> fileStore;

    public FileMessageRepository() {
        this.fileStore = new FileStore<>(FILE_PATH, TARGET_NAME);
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
    public List<Message> findByChannelId(UUID channelId) {
        return loadOrEmpty().values().stream()
            .filter(message -> message.getChannelId().equals(channelId))
            .sorted(Comparator.comparing(Message::getCreatedAt))
            .toList();
    }

    @Override
    public Optional<Message> findRecentlyByChannelId(UUID channelId) {
        return loadOrEmpty().values().stream()
            .filter(message -> message.getChannelId().equals(channelId))
            .max(Comparator.comparing(Message::getCreatedAt));
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> data = loadOrEmpty();
        data.remove(id);
        fileStore.save(data);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Map<UUID, Message> data = loadOrEmpty();

        data.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
        fileStore.save(data);
    }

    private Map<UUID, Message> loadOrEmpty() {
        Map<UUID, Message> data = fileStore.load();
        return data == null ? new HashMap<>() : data;
    }
}
