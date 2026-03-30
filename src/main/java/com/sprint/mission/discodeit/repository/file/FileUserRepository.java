package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private final File file;

    public FileUserRepository(String filePath) {
        this.file = new File(filePath);
    }

    @Override
    public User save(User user) {
        Map<UUID, User> data = load();
        data.put(user.getId(), user);
        save(data);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(load().get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(load().values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> data = load();
        data.remove(id);
        save(data);
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, User> load() {
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (EOFException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("failed file read", e);
        }
    }

    private void save(Map<UUID, User> data) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("User 파일 저장 실패", e);
        }
    }
}
