package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.custom.BadRequestException;
import com.sprint.mission.discodeit.exception.custom.FileProcessingException;
import com.sprint.mission.discodeit.exception.custom.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponse create(UserCreateRequest request, MultipartFile profileImg) {
        validateDuplicateUser(request);

        User user = new User(
            request.email(),
            request.username(),
            request.password(),
            null
        );

        if (profileImg != null) {
            UUID profileImageId = saveProfileImage(user, profileImg);
            user.updateProfileImageId(profileImageId);
        }

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }

    @Override
    public UserResponse find(UUID id) {
        User user = getUserOrThrow(id);

        return UserResponse.from(user);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
            .map(user -> UserDto.from(user, getUserStatusOrThrow(user.getId())))
            .toList();
    }

    @Override
    public UserResponse update(UUID userId, UserUpdateRequest request, MultipartFile profileImg) {
        User user = getUserOrThrow(userId);

        validateDuplicateForUpdate(userId, request);

        UUID profileImageId = user.getProfileImageId();
        if (profileImg != null) {
            if (profileImageId != null) {
                binaryContentRepository.delete(profileImageId);
            }

            profileImageId = saveProfileImage(user, profileImg);
        }

        user.changeProfile(
            request.email(),
            request.username(),
            profileImageId
        );
        userRepository.save(user);

        return UserResponse.from(user);
    }

    @Override
    public void delete(UUID id) {
        User user = getUserOrThrow(id);

        if (user.getProfileImageId() != null) {
            binaryContentRepository.delete(user.getProfileImageId());
        }

        userStatusRepository.findByUserId(user.getId())
            .ifPresent(userStatus -> userStatusRepository.delete(userStatus.getId()));

        userRepository.delete(id);
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.format(userId))
        );
    }

    private UserStatus getUserStatusOrThrow(UUID userId) {
        return userStatusRepository.findByUserId(userId).orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.USERSTATUS_WITH_USERID_NOT_FOUND.format(userId))
        );
    }

    private void validateDuplicateUser(UserCreateRequest request) {
        if (userRepository.findByUserName(request.username()).isPresent()
            || userRepository.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException(ErrorCode.USER_DUPLICATE.getMessage());
        }
    }

    private void validateDuplicateForUpdate(UUID userId, UserUpdateRequest request) {
        userRepository.findByEmail(request.email())
            .filter(found -> !found.getId().equals(userId))
            .ifPresent(found -> {
                throw new BadRequestException(ErrorCode.USER_EMAIL_ALREADY_EXIST.format(found.getEmail()));
            });

        userRepository.findByUserName(request.username())
            .filter(found -> !found.getId().equals(userId))
            .ifPresent(found -> {
                throw new BadRequestException(ErrorCode.USER_USERNAME_ALREADY_EXIST.format(found.getUserName()));
            });
    }

    private UUID saveProfileImage(User user, MultipartFile profileImg) {
        try{
            BinaryContent profileImage = new BinaryContent(
                profileImg.getOriginalFilename(),
                profileImg.getContentType(),
                profileImg.getBytes(),
                user.getId(),
                null
            );
            BinaryContent savedBinaryContent = binaryContentRepository.save(profileImage);
            return savedBinaryContent.getId();
        } catch (IOException e){
            throw new FileProcessingException(ErrorCode.FILE_PROCESSING_ERROR.getMessage(), e);
        }
    }
}
