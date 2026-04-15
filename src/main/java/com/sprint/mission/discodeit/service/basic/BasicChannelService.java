package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
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
        Channel channel = new Channel(
            request.name(),
            ChannelType.PUBLIC,
            request.notiTitle(),
            request.notiContents()
        );

        return channelResponseDtoFrom(channelRepository.save(channel));
    }

    @Override
    public ChannelResponse createPrivate(ChannelPrivateCreateRequest request) {
        Channel channel = new Channel(
            null,
            ChannelType.PRIVATE,
            null,
            null
        );

        List<UUID> users = new ArrayList<>();
        for (UUID userId : request.memberUserIds()) {
            User user = getUserOrThrow(userId);

            users.add(user.getId());

            ReadStatus readStatus = new ReadStatus(
                user.getId(),
                channel.getId()
            );
            readStatusRepository.save(readStatus);
        }

        channel.addUsers(users);
        channelRepository.save(channel);
        return channelResponseDtoFrom(channel);
    }

    @Override
    public ChannelFindResponse find(UUID id) {
        Channel channel = getChannelOrThrow(id);
        Message recentMessage = messageRepository.findRecentlyByChannelId(channel.getId())
            .orElse(null);

        return channelFindResponseDtoFrom(channel, recentMessage);
    }

    @Override
    public List<ChannelFindResponse> findAllByUserId(UUID userId) {
        getUserOrThrow(userId);

        List<Channel> channels = channelRepository.findAll();
        List<ChannelFindResponse> responses = new ArrayList<>();
        for (Channel channel : channels) {
            if (!availableAccessChannel(channel, userId)) {
                continue;
            }

            Message recentMessage = messageRepository.findRecentlyByChannelId(channel.getId())
                .orElse(null);
            responses.add(channelFindResponseDtoFrom(channel, recentMessage));
        }

        return responses;
    }

    @Override
    public ChannelResponse update(ChannelUpdateRequest request) {
        Channel channel = getChannelOrThrow(request.id());

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new BadRequestException(ErrorCode.PRIVATE_CHANNEL_CANNOT_UPDATE.getMessage());
        }

        channel.updateChannel(request.name(), request.notiTitle(), request.notiContents());
        return channelResponseDtoFrom(channel);
    }

    @Override
    public void delete(UUID id) {
        getChannelOrThrow(id);

        messageRepository.deleteByChannelId(id);
        readStatusRepository.deleteByChannelId(id);
        channelRepository.delete(id);
    }

    private Channel getChannelOrThrow(UUID channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CHANNEL_NOT_FOUND.format(channelId)));
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.format(userId)));
    }

    private boolean availableAccessChannel(Channel channel, UUID userId) {
        if (channel.getType() == ChannelType.PUBLIC) {
            return true;
        }

        return channel.getType() == ChannelType.PRIVATE
            && channel.getUsers().contains(userId);
    }

    private ChannelResponse channelResponseDtoFrom(Channel channel) {
        return new ChannelResponse(
            channel.getId(),
            channel.getName(),
            channel.getType()
        );
    }

    private ChannelFindResponse channelFindResponseDtoFrom(Channel channel, Message recentMessage) {
        Instant messageCreatedAt = recentMessage == null ? null : recentMessage.getUpdatedAt();

        List<UUID> users = new ArrayList<>();
        if (channel.getType() == ChannelType.PRIVATE) {
            users = new ArrayList<>(channel.getUsers());
        }

        return new ChannelFindResponse(
            channel.getId(),
            channel.getName(),
            channel.getType(),
            messageCreatedAt,
            users
        );
    }
}
