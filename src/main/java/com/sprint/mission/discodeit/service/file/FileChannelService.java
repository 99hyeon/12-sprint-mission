package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.FileStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private final FileStore<Map<UUID, Channel>> fileStore;

    public FileChannelService(String filePath) {
        this.fileStore = new FileStore<>(filePath, "Channel");
    }

    @Override
    public Channel create(Channel channel) {
        Map<UUID, Channel> data = loadOrEmpty();
        data.put(channel.getId(), channel);
        fileStore.save(data);
        return channel;
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return Optional.ofNullable(loadOrEmpty().get(id));
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(loadOrEmpty().values());
    }

    @Override
    public Channel update(Channel channel) {
        Map<UUID, Channel> data = loadOrEmpty();
        if (!data.containsKey(channel.getId())) {
            return null;
        }
        data.put(channel.getId(), channel);
        fileStore.save(data);
        return channel;
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
