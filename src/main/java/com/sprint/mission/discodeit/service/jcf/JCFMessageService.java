package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final UserService userService;
    private final ChannelService channelService;

    private final Map<UUID, Message> data;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        this.data = new HashMap<>();
    }

    @Override
    public Message create(Message message) {
        checkValidation(message);

        if(data.containsKey(message.getId())) return data.get(message.getId());

        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(Message message) {
        checkValidation(message);

        if(data.containsKey(message.getId())) {
            data.put(message.getId(), message);

            return message;
        } else {
            throw new IllegalArgumentException("존재하지 않는 메세지");
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    private void checkValidation(Message message) {
        userService.read(message.getUser().getId()).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 사용자")
        );
        channelService.read(message.getChannel().getId()).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 채널")
        );
    }
}
