package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

        //todo: 이거 엔티티에서 만들어서 반환해주는게 좋나?
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

    //todo: 이거 list로 담아서 보내나? 그냥 리스트를 담은 dto를 보내는거 아닌가?
    @Override
    public List<ReadStatusResponse> findByUserId(UUID userId) {
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
            () -> new IllegalArgumentException("readStatus 존재 안 함")
        );
    }

    private void validateUserExists(UUID userId) {
        userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("유저 존재 안 함")
        );
    }

    private void validateChannelExists(UUID channelId) {
        channelRepository.findById(channelId).orElseThrow(
            () -> new IllegalArgumentException("채널 존재 안 함")
        );
    }

    private void validateReadStatusNotExists(UUID userId, UUID channelId) {
        readStatusRepository.findByUserIdAndChannelId(userId, channelId)
            .ifPresent(readStatus -> {
                throw new IllegalArgumentException("readStatus 이미 존재 함");
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
