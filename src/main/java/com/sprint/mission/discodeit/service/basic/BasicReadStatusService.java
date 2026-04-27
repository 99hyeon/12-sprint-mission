package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.custom.BadRequestException;
import com.sprint.mission.discodeit.exception.custom.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusResponse create(ReadStatusCreateRequest request) {
        validateUserExists(request.userId());
        Channel channel = getChannelOrThrow(request.channelId());
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new BadRequestException(
                ErrorCode.READSTATUS_ALREADY_EXIST.format(request.userId(), request.channelId()));
        }
        validateReadStatusNotExists(request.userId(), request.channelId());

        ReadStatus readStatus = new ReadStatus(
            request.userId(),
            request.channelId(),
            request.lastReadAt()
        );

        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);
        return ReadStatusResponse.from(savedReadStatus);
    }

    @Override
    public ReadStatusResponse find(UUID id) {
        ReadStatus readStatus = getReadStatusOrThrow(id);

        return ReadStatusResponse.from(readStatus);
    }

    @Override
    public List<ReadStatusResponse> findAllByUserId(UUID userId) {
        validateUserExists(userId);
        List<ReadStatus> readStatuses = readStatusRepository.findByUserId(userId);

        return readStatuses.stream()
            .map(ReadStatusResponse::from)
            .toList();
    }

    @Override
    public ReadStatusResponse update(UUID userStatusId, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = getReadStatusOrThrow(userStatusId);

        readStatus.changeReadStatus(request.newLastReadAt());
        ReadStatus changedReadStatus = readStatusRepository.save(readStatus);
        return ReadStatusResponse.from(changedReadStatus);
    }

    @Override
    public void delete(UUID id) {
        getReadStatusOrThrow(id);
        readStatusRepository.delete(id);
    }

    private ReadStatus getReadStatusOrThrow(UUID id) {
        return readStatusRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.READSTATUS_NOT_FOUND.format(id))
        );
    }

    private Channel getChannelOrThrow(UUID channelId) {
        return channelRepository.findById(channelId).orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.CHANNEL_NOT_FOUND.format(channelId))
        );
    }

    private void validateUserExists(UUID userId) {
        userRepository.findById(userId).orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.format(userId))
        );
    }

    private void validateReadStatusNotExists(UUID userId, UUID channelId) {
        readStatusRepository.findByUserIdAndChannelId(userId, channelId)
            .ifPresent(readStatus -> {
                throw new BadRequestException(
                    ErrorCode.READSTATUS_ALREADY_EXIST.format(userId, channelId));
            });
    }
}
