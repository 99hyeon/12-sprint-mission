package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FileStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
    prefix = "discodeit.repository",
    name = "type",
    havingValue = "file"
)
public class FileChannelRepository implements ChannelRepository {
    private static final String TARGET_NAME = "Channel";
    private static final String FILE_PATH = "/channels.ser";


    private final FileStore<Map<UUID, Channel>> fileStore;

    public FileChannelRepository(
        @Value("${discodeit.repository.file-directory:data}") String fileDirectory
    ) {
        this.fileStore = new FileStore<>(fileDirectory + FILE_PATH, TARGET_NAME);
    }

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> data = loadOrEmpty();
        data.put(channel.getId(), channel);
        fileStore.save(data);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(loadOrEmpty().get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(loadOrEmpty().values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> data = loadOrEmpty();
        data.remove(id);
        fileStore.save(data);
    }

    private Map<UUID, Channel> loadOrEmpty() {
        Map<UUID, Channel> data = fileStore.load();
        return data == null ? new HashMap<>() : data;
    }
}
