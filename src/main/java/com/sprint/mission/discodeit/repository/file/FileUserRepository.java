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

public class FileUserRepository implements UserRepository {
    private final FileStore<Map<UUID, User>> fileStore;

    public FileUserRepository(String filePath) {
        this.fileStore = new FileStore<>(filePath, "User");
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
