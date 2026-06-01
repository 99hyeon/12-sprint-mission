package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
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
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public UserResponse create(UserCreateRequest request, MultipartFile profile) {
    validateDuplicateUser(request);

    BinaryContent binaryContent = null;
    if (profile != null) {
      binaryContent = saveProfileImage(profile);
    }

    User user = new User(
        request.email(),
        request.username(),
        request.password(),
        binaryContent
    );
    user.initStatus();
    User savedUser = userRepository.save(user);

    return UserResponse.from(savedUser);
  }

  @Override
  public List<UserResponse> findAll() {
    return userRepository.findAll().stream()
        .map(user -> UserResponse.from(user))
        .toList();
  }

  @Override
  public UserResponse update(UUID userId, UserUpdateRequest request, MultipartFile profile) {
    User user = getUserOrThrow(userId);
    validateDuplicateForUpdate(userId, request);

    UUID profileImageId = user.getProfile().getId();
    BinaryContent binaryContent = null;
    if (profile != null) {
      if (profileImageId != null) {
        binaryContentRepository.deleteById(profileImageId);
      }

      binaryContent = saveProfileImage(profile);
    }

    user.changeProfile(
        request.newEmail(),
        request.newUsername(),
        binaryContent
    );
    userRepository.save(user);
    UserStatus userStatus = getUserStatusOrThrow(user.getId());

    return UserResponse.from(user);
  }

  @Override
  public void delete(UUID id) {
    User user = getUserOrThrow(id);

    if (user.getProfile().getId() != null) {
      binaryContentRepository.deleteById(user.getProfile().getId());
    }

    userStatusRepository.findByUserId(user.getId())
        .ifPresent(userStatus -> userStatusRepository.deleteById(userStatus.getId()));

    userRepository.deleteById(id);
  }

  private User getUserOrThrow(UUID userId) {
    return userRepository.findById(userId).orElseThrow(
        () -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.format(userId))
    );
  }

  private UserStatus getUserStatusOrThrow(UUID userId) {
    return userStatusRepository.findByUserId(userId).orElseThrow(
        () -> new ResourceNotFoundException(
            ErrorCode.USERSTATUS_WITH_USERID_NOT_FOUND.format(userId))
    );
  }

  private void validateDuplicateUser(UserCreateRequest request) {
    if (userRepository.findByUsername(request.username()).isPresent()
        || userRepository.findByEmail(request.email()).isPresent()) {
      throw new BadRequestException(ErrorCode.USER_DUPLICATE.getMessage());
    }
  }

  private void validateDuplicateForUpdate(UUID userId, UserUpdateRequest request) {
    userRepository.findByEmail(request.newEmail())
        .filter(found -> !found.getId().equals(userId))
        .ifPresent(found -> {
          throw new BadRequestException(
              ErrorCode.USER_EMAIL_ALREADY_EXIST.format(found.getEmail()));
        });

    userRepository.findByUsername(request.newUsername())
        .filter(found -> !found.getId().equals(userId))
        .ifPresent(found -> {
          throw new BadRequestException(
              ErrorCode.USER_USERNAME_ALREADY_EXIST.format(found.getUsername()));
        });
  }

  private BinaryContent saveProfileImage(MultipartFile profile) {
    try {
      BinaryContent binaryContent = new BinaryContent(
          profile.getOriginalFilename(),
          profile.getContentType(),
          profile.getSize()
      );

      BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

      binaryContentStorage.put(
          savedBinaryContent.getId(),
          profile.getBytes()
      );

      return savedBinaryContent;
    } catch (IOException e) {
      throw new FileProcessingException(ErrorCode.FILE_PROCESSING_ERROR.getMessage(), e);
    }
  }
}
