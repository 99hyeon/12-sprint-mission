package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(Channel channel) {
        return channelRepository.save(channel);
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(Channel channel) {
        channelRepository.findById(channel.getId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널"));
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }
}
