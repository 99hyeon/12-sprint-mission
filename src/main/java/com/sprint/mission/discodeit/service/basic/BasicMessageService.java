package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public BasicMessageService(MessageRepository messageRepository, UserService userService,
        ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Optional<Message> read(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(Message message) {
        validateMessage(message);
        messageRepository.findById(message.getId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메세지"));
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }

    private void validateMessage(Message message) {
        userService.read(message.getUser().getId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        channelService.read(message.getChannel().getId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널"));
    }
}
