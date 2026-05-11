package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.custom.FileProcessingException;
import com.sprint.mission.discodeit.exception.custom.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public MessageResponse create(MessageCreateRequest request, List<MultipartFile> attachments) {
        validateUserExists(request.authorId());
        validateChannelExists(request.channelId());

        Message message = new Message(
            request.content(),
            request.channelId(),
            request.authorId()
        );
        messageRepository.save(message);

        List<BinaryContent> binaryContents = new ArrayList<>();
        if (attachments != null) {
            for (MultipartFile file : attachments) {
                try {
                    BinaryContent binaryContent = new BinaryContent(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes(),
                        null,
                        message.getId()
                    );
                    binaryContents.add(binaryContent);

                } catch (IOException e) {
                    throw new FileProcessingException(ErrorCode.FILE_PROCESSING_ERROR.getMessage(),
                        e);
                }
            }
        }
        binaryContentRepository.saveAll(binaryContents);

        return MessageResponse.from(message, binaryContents);
    }

    @Override
    public MessageResponse find(UUID id) {
        Message message = getMessageOrThrow(id);
        List<BinaryContent> files = binaryContentRepository.findByMessageId(id);

        return MessageResponse.from(message, files);
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        validateChannelExists(channelId);

        List<Message> messages = messageRepository.findByChannelId(channelId);
        List<MessageResponse> responses = new ArrayList<>();
        for (Message message : messages) {
            List<BinaryContent> files = binaryContentRepository.findByMessageId(message.getId());
            responses.add(MessageResponse.from(message, files));
        }

        return responses;
    }

    @Override
    public MessageResponse update(UUID messageId, MessageUpdateRequest request) {
        Message message = getMessageOrThrow(messageId);
        validateUserExists(message.getUserId());
        validateChannelExists(message.getChannelId());

        message.changeContent(request.newContent());
        message = messageRepository.save(message);
        List<BinaryContent> files = binaryContentRepository.findByMessageId(messageId);

        return MessageResponse.from(message, files);
    }

    @Override
    public void delete(UUID id) {
        getMessageOrThrow(id);

        binaryContentRepository.deleteAllByMessageId(id);
        messageRepository.delete(id);
    }

    private Message getMessageOrThrow(UUID messageId) {
        return messageRepository.findById(messageId)
            .orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.MESSAGE_NOT_FOUND.format(messageId)));
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
}
