package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.util.FileStore;
import java.util.ArrayList;
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
public class FileUserStatusRepository implements UserStatusRepository {

    private static final String TARGET_NAME = "UserStatus";
    private static final String FILE_PATH = "data/userStatuses.ser";

    private final FileStore<Map<UUID, UserStatus>> fileStore;

    public FileUserStatusRepository() {
        this.fileStore = new FileStore<>(FILE_PATH, TARGET_NAME);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Map<UUID, UserStatus> data = loadOrEmpty();
        data.put(userStatus.getId(), userStatus);
        fileStore.save(data);

        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(loadOrEmpty().get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return loadOrEmpty().values().stream()
            .filter(userStatus -> userStatus.getUserId().equals(userId))
            .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(loadOrEmpty().values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, UserStatus> data = loadOrEmpty();
        data.remove(id);
        fileStore.save(data);
    }

    private Map<UUID, UserStatus> loadOrEmpty() {
        Map<UUID, UserStatus> data = fileStore.load();
        return data == null ? new HashMap<>() : data;
    }
}
