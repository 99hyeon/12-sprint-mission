package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.custom.FileProcessingException;
import com.sprint.mission.discodeit.exception.custom.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private static final int MESSAGE_PAGE_SIZE = 50;

  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public MessageResponse create(MessageCreateRequest request, List<MultipartFile> attachments) {
    User author = getUserOrThrow(request.authorId());
    Channel channel = getChannelOrThrow(request.channelId());

    List<BinaryContent> savedBinaryContents = saveAttachments(attachments);

    Message message = new Message(
        request.content(),
        channel,
        author,
        savedBinaryContents
    );

    Message savedMessage = messageRepository.save(message);

    return MessageResponse.from(savedMessage);
  }

  @Override
  public PageResponse<MessageResponse> findAllByChannelId(UUID channelId, int page) {
    Channel channel = getChannelOrThrow(channelId);

    Pageable pageable = PageRequest.of(page, MESSAGE_PAGE_SIZE);

    Slice<MessageResponse> messageResponses = messageRepository
        .findAllByChannelOrderByCreatedAtDesc(channel, pageable)
        .map(MessageResponse::from);

    return PageResponse.from(messageResponses);
  }

  @Override
  public MessageResponse update(UUID messageId, MessageUpdateRequest request) {
    Message message = getMessageOrThrow(messageId);
    validateUserExists(message.getAuthor().getId());
    validateChannelExists(message.getChannel().getId());

    message.changeContent(request.newContent());
    message = messageRepository.save(message);

    return MessageResponse.from(message);
  }

  @Override
  public void delete(UUID id) {
    Message message = getMessageOrThrow(id);
    List<BinaryContent> attachments = message.getAttachments();

    if (!attachments.isEmpty()) {
      binaryContentRepository.deleteAll(attachments);
    }
    messageRepository.deleteById(id);
  }

  private User getUserOrThrow(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.format(userId)));
  }

  private Channel getChannelOrThrow(UUID channelId) {
    return channelRepository.findById(channelId).orElseThrow(
        () -> new ResourceNotFoundException(ErrorCode.CHANNEL_NOT_FOUND.format(channelId))
    );
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

  private List<BinaryContent> saveAttachments(List<MultipartFile> attachments) {
    if (attachments == null || attachments.isEmpty()) {
      return List.of();
    }

    List<BinaryContent> savedBinaryContents = new ArrayList<>();

    for (MultipartFile file : attachments) {
      try {
        BinaryContent binaryContent = new BinaryContent(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize()
        );

        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

        binaryContentStorage.put(
            savedBinaryContent.getId(),
            file.getBytes()
        );

        savedBinaryContents.add(savedBinaryContent);
      } catch (IOException e) {
        throw new FileProcessingException(
            ErrorCode.FILE_PROCESSING_ERROR.getMessage(),
            e
        );
      }
    }

    return savedBinaryContents;
  }

}
