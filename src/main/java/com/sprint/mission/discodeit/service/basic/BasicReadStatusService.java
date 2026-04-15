package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
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
        validateChannelExists(request.channelId());
        validateReadStatusNotExists(request.userId(), request.channelId());

        ReadStatus readStatus = new ReadStatus(
            request.userId(),
            request.channelId()
        );

        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);
        return dtoFrom(savedReadStatus);
    }

    @Override
    public ReadStatusResponse find(UUID id) {
        ReadStatus readStatus = getReadStatusOrThrow(id);

        return dtoFrom(readStatus);
    }

    @Override
    public List<ReadStatusResponse> findAllByUserId(UUID userId) {
        validateUserExists(userId);
        List<ReadStatus> readStatuses = readStatusRepository.findByUserId(userId);

        return readStatuses.stream()
            .map(this::dtoFrom)
            .toList();
    }

    @Override
    public ReadStatusResponse update(ReadStatusUpdateRequest request) {
        ReadStatus readStatus = getReadStatusOrThrow(request.id());

        validateUserExists(request.userId());
        validateChannelExists(request.channelId());

        readStatus.updateReadStatus(request.userId(), request.channelId());
        ReadStatus updatedReadStatus = readStatusRepository.save(readStatus);
        return dtoFrom(updatedReadStatus);
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

    private void validateReadStatusNotExists(UUID userId, UUID channelId) {
        readStatusRepository.findByUserIdAndChannelId(userId, channelId)
            .ifPresent(readStatus -> {
                throw new BadRequestException(ErrorCode.READSTATUS_ALREADY_EXIST.format(userId, channelId));
            });
    }

    private ReadStatusResponse dtoFrom(ReadStatus readStatus){
        return new ReadStatusResponse(
            readStatus.getId(),
            readStatus.getUserId(),
            readStatus.getChannelId()
        );
    }
}
