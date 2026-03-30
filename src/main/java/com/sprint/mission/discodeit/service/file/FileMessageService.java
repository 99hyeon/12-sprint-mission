package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
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

public class FileMessageService implements MessageService {

    private final File file;

    public FileMessageService(String filePath) {
        this.file = new File(filePath);
    }


    @Override
    public Message create(Message message) {
        Map<UUID, Message> data = load();
        data.put(message.getId(), message);
        save(data);
        return message;
    }

    @Override
    public Optional<Message> read(UUID id) {
        Map<UUID, Message> data = load();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(load().values());
    }

    @Override
    public Message update(Message message) {
        Map<UUID, Message> data = load();
        if (!data.containsKey(message.getId())) {
            return null;
        }
        data.put(message.getId(), message);
        save(data);
        return message;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> data = load();
        data.remove(id);
        save(data);
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Message> load() {
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (EOFException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("failed file read", e);
        }
    }

    private void save(Map<UUID, Message> data) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Channel 파일 저장 실패", e);
        }
    }
}
