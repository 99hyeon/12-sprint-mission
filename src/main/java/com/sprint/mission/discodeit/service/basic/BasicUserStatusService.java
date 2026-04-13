package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatusResponse create(UserStatusCreateRequest request) {
        validateUserExists(request.userId());
        validateUserStatusNotExists(request.userId());

        UserStatus userStatus = new UserStatus(
            request.userId()
        );
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        return dtoFrom(savedUserStatus);
    }

    @Override
    public UserStatusResponse find(UUID id) {
        UserStatus userStatus = getUserStatusOrThrow(id);

        return dtoFrom(userStatus);
    }

    @Override
    public List<UserStatusResponse> findAll() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();

        return userStatuses.stream()
            .map(this::dtoFrom)
            .toList();
    }

    @Override
    public UserStatusResponse update(UserStatusUpdateRequest request) {
        UserStatus userStatus = getUserStatusOrThrow(request.id());

        userStatus.updateUpdatedAt();
        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);

        return dtoFrom(updatedUserStatus);
    }

    @Override
    public UserStatusResponse updateByUserId(UUID userId) {
        UserStatus userStatus = getUserStatusByUserIdOrThrow(userId);

        userStatus.updateUpdatedAt();
        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);

        return dtoFrom(updatedUserStatus);
    }

    @Override
    public void delete(UUID id) {
        getUserStatusOrThrow(id);
        userStatusRepository.delete(id);
    }

    private UserStatus getUserStatusOrThrow(UUID id) {
        return userStatusRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("userStatus 존재 안 함")
        );
    }

    private UserStatus getUserStatusByUserIdOrThrow(UUID userId) {
        return userStatusRepository.findByUserId(userId).orElseThrow(
            () -> new IllegalArgumentException("userStatus 존재 안 함")
        );
    }

    private void validateUserExists(UUID userId) {
        userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("user 존재 안 함")
        );
    }

    private void validateUserStatusNotExists(UUID userId) {
        userStatusRepository.findByUserId(userId).ifPresent(userStatus -> {
            throw new IllegalArgumentException("userStatus 이미 존재함");
        });
    }

    private UserStatusResponse dtoFrom(UserStatus userStatus){
        return new UserStatusResponse(
            userStatus.getId(),
            userStatus.getUserId(),
            userStatus.isOnline()
        );
    }
}
