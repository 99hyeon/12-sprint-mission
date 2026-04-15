package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.custom.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public MessageResponse create(MessageCreateRequest request) {
        validateUserExists(request.userId());
        validateChannelExists(request.channelId());

        Message message = new Message(
            request.content(),
            request.channelId(),
            request.userId()
        );
        messageRepository.save(message);

        List<BinaryContent> files = new ArrayList<>();
        for (BinaryContentCreateRequest file : request.files()) {
            BinaryContent binaryContent = new BinaryContent(
                file.fileName(),
                file.contentType(),
                file.data(),
                null,
                message.getId()
            );

            files.add(binaryContent);
        }
        binaryContentRepository.saveAll(files);

        return dtoFrom(message, files);
    }

    @Override
    public MessageResponse find(UUID id) {
        Message message = getMessageOrThrow(id);
        List<BinaryContent> files = binaryContentRepository.findByMessageId(id);

        return dtoFrom(message, files);
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        validateChannelExists(channelId);

        List<Message> messages = messageRepository.findByChannelId(channelId);
        List<MessageResponse> responses = new ArrayList<>();
        for (Message message : messages) {
            List<BinaryContent> files = binaryContentRepository.findByMessageId(message.getId());
            responses.add(dtoFrom(message, files));
        }

        return responses;
    }

    @Override
    public MessageResponse update(MessageUpdateRequest request) {
        Message message = getMessageOrThrow(request.id());
        validateUserExists(message.getUserId());
        validateChannelExists(message.getChannelId());

        message.updateContent(request.content());
        message = messageRepository.save(message);
        List<BinaryContent> files = binaryContentRepository.findByMessageId(request.id());

        return dtoFrom(message, files);
    }

    @Override
    public void delete(UUID id) {
        getMessageOrThrow(id);

        binaryContentRepository.deleteAllByMessageId(id);
        messageRepository.delete(id);
    }

    private Message getMessageOrThrow(UUID messageId) {
        return messageRepository.findById(messageId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MESSAGE_NOT_FOUND.format(messageId)));
    }

    private void validateUserExists(UUID userId) {
        userRepository.findById(userId).orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.format(userId))
        );
    }

    private void validateChannelExists(UUID channelId) {
        channelRepository.findById(channelId).orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.CHANNEL_NOT_FOUND.format(channelId))
        );
    }

    private MessageResponse dtoFrom(Message message, List<BinaryContent> files) {
        return new MessageResponse(
            message.getId(),
            message.getContent(),
            message.getChannelId(),
            message.getUserId(),
            dtoFrom(files)
        );
    }

    private List<BinaryContentResponse> dtoFrom(List<BinaryContent> files) {
        return files.stream()
            .map(file -> new BinaryContentResponse(
                file.getId(),
                file.getFileName(),
                file.getContentType(),
                file.getData(),
                file.getUserId(),
                file.getMessageId()
            ))
            .toList();
    }
}
