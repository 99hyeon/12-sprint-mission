package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data;

    public JCFMessageService() {
        data = new HashMap<>();
    }

    @Override
    public Message create(Message message) {
        if(data.containsKey(message.getId())) return data.get(message.getId());

        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll(UUID id) {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(Message message) {
        if(data.containsKey(message.getId())) {
            data.put(message.getId(), message);

            return message;
        }

        return null;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
