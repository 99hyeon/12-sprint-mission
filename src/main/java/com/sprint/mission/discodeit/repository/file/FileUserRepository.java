package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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
public class FileUserRepository implements UserRepository {
    private static final String TARGET_NAME = "User";
    private static final String FILE_PATH = "/users.ser";


    private final FileStore<Map<UUID, User>> fileStore;

    public FileUserRepository(
        @Value("${discodeit.repository.file-directory:data}") String fileDirectory
    ) {
        this.fileStore = new FileStore<>(fileDirectory + FILE_PATH, TARGET_NAME);
    }

    @Override
    public User save(User user) {
        Map<UUID, User> data = loadOrEmpty();
        data.put(user.getId(), user);
        fileStore.save(data);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(loadOrEmpty().get(id));
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return loadOrEmpty().values().stream()
            .filter(user -> user.getUserName().equals(userName))
            .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return loadOrEmpty().values().stream()
            .filter(user -> user.getEmail().equals(email))
            .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(loadOrEmpty().values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> data = loadOrEmpty();
        data.remove(id);
        fileStore.save(data);
    }

    private Map<UUID, User> loadOrEmpty() {
        Map<UUID, User> data = fileStore.load();
        return data == null ? new HashMap<>() : data;
    }
}
