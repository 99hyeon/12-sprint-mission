package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.custom.BadRequestException;
import com.sprint.mission.discodeit.exception.custom.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  @Override
  public ChannelResponse createPublic(ChannelPublicCreateRequest request) {
    Channel channel = Channel.createPublic(
        request.name(),
        request.description()
    );

    return ChannelResponse.from(channelRepository.save(channel), List.of(), Instant.now());
  }

  @Override
  public ChannelResponse createPrivate(ChannelPrivateCreateRequest request) {
    Channel channel = Channel.createPrivate(request.name());
    Channel savedChannel = channelRepository.save(channel);

    List<UserResponse> participants = new ArrayList<>();
    for (UUID userId : request.participantIds()) {
      User user = getUserOrThrow(userId);

      ReadStatus readStatus = new ReadStatus(
          user,
          savedChannel,
          Instant.now()
      );
      readStatusRepository.save(readStatus);
      participants.add(UserResponse.from(user));
    }

    return ChannelResponse.from(savedChannel, participants, Instant.now());
  }

  @Override
  public List<ChannelResponse> findAllByUserId(UUID userId) {
    getUserOrThrow(userId);

    List<Channel> channels = channelRepository.findAll();
    List<ChannelResponse> responses = new ArrayList<>();
    for (Channel channel : channels) {
      if (!availableAccessChannel(channel, userId)) {
        continue;
      }

      List<UserResponse> participants = getParticipants(channel);
      Instant lastMessageAt = getLastMessageAt(channel);

      responses.add(ChannelResponse.from(channel, participants, lastMessageAt));
    }

    return responses;
  }

  @Override
  public ChannelResponse update(UUID channelId, ChannelUpdateRequest request) {
    Channel channel = getChannelOrThrow(channelId);

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new BadRequestException(ErrorCode.PRIVATE_CHANNEL_CANNOT_UPDATE.getMessage());
    }

    channel.changeChannel(request.newName(), request.newDescription());
    Channel updatedChannel = channelRepository.save(channel);

    return ChannelResponse.from(updatedChannel, List.of(), getLastMessageAt(channel));
  }

  @Override
  public void delete(UUID id) {
    getChannelOrThrow(id);

    messageRepository.deleteByChannelId(id);
    readStatusRepository.deleteByChannelId(id);
    channelRepository.deleteById(id);
  }

  private Channel getChannelOrThrow(UUID channelId) {
    return channelRepository.findById(channelId)
        .orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.CHANNEL_NOT_FOUND.format(channelId)));
  }

  private User getUserOrThrow(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.format(userId)));
  }

  private boolean availableAccessChannel(Channel channel, UUID userId) {
    if (channel.getType() == ChannelType.PUBLIC) {
      return true;
    }

    if (channel.getType() == ChannelType.PRIVATE) {
      return readStatusRepository.findByUserIdAndChannelId(userId, channel.getId())
          .isPresent();
    }

    return false;
  }

  private List<UserResponse> getParticipants(Channel channel) {
    if (channel.getType() == ChannelType.PUBLIC) {
      return List.of();
    }

    return readStatusRepository.findAllByChannelId(channel.getId()).stream()
        .map(ReadStatus::getUser)
        .map(UserResponse::from)
        .toList();
  }

  private Instant getLastMessageAt(Channel channel) {
    return messageRepository.findTopByChannelOrderByCreatedAtDesc(channel)
        .map(Message::getCreatedAt)
        .orElse(null);
  }

}
