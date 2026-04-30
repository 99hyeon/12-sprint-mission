package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class FileReadStatusRepository implements ReadStatusRepository {
    private static final String TARGET_NAME = "ReadStatus";
    private static final String FILE_PATH = "/readStatuses.ser";


    private final FileStore<Map<UUID, ReadStatus>> fileStore;

    public FileReadStatusRepository(
        @Value("${discodeit.repository.file-directory:data}") String fileDirectory
    ) {
        this.fileStore = new FileStore<>(fileDirectory + FILE_PATH, TARGET_NAME);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Map<UUID, ReadStatus> data = loadOrEmpty();
        data.put(readStatus.getId(), readStatus);
        fileStore.save(data);

        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(loadOrEmpty().get(id));
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return loadOrEmpty().values().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId))
            .toList();
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return loadOrEmpty().values().stream()
            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
            .toList();
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return loadOrEmpty().values().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId))
            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
            .findFirst();
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(loadOrEmpty().values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, ReadStatus> data = loadOrEmpty();
        data.remove(id);
        fileStore.save(data);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> data = loadOrEmpty();

        data.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
        fileStore.save(data);
    }

    private Map<UUID, ReadStatus> loadOrEmpty() {
        Map<UUID, ReadStatus> data = fileStore.load();
        return data == null ? new HashMap<>() : data;
    }
}
