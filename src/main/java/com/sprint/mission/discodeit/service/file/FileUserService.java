package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FileStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {
    private final FileStore<Map<UUID, User>> fileStore;

    public FileUserService(String filePath) {
        this.fileStore = new FileStore<>(filePath, "User");
    }

    @Override
    public User create(User user) {
        Map<UUID, User> data = loadOrEmpty();
        data.put(user.getId(), user);
        fileStore.save(data);
        return user;
    }

    @Override
    public Optional<User> read(UUID id) {
        return Optional.ofNullable(loadOrEmpty().get(id));
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(loadOrEmpty().values());
    }

    @Override
    public User update(User user) {
        Map<UUID, User> data = loadOrEmpty();
        if (!data.containsKey(user.getId())) {
            return null;
        }
        data.put(user.getId(), user);
        fileStore.save(data);
        return user;
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
