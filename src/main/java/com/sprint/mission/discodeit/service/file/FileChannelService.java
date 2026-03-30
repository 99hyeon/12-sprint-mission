package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
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

public class FileChannelService implements ChannelService {

    private final File file;

    public FileChannelService(String filePath) {
        this.file = new File(filePath);
    }

    @Override
    public Channel create(Channel channel) {
        Map<UUID, Channel> data = load();
        data.put(channel.getId(), channel);
        save(data);
        return channel;
    }

    @Override
    public Optional<Channel> read(UUID id) {
        Map<UUID, Channel> data = load();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(load().values());
    }

    @Override
    public Channel update(Channel channel) {
        Map<UUID, Channel> data = load();
        if (!data.containsKey(channel.getId())) {
            return null;
        }
        data.put(channel.getId(), channel);
        save(data);
        return channel;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> data = load();
        data.remove(id);
        save(data);
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> load() {
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (EOFException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("failed file read", e);
        }
    }

    private void save(Map<UUID, Channel> data) {
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
