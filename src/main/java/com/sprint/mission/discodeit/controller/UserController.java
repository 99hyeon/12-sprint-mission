package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/api/users", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> createUser(
        @RequestPart("userCreateRequest") UserCreateRequest request,
        @RequestPart(value = "profileImg", required = false) MultipartFile profileImg
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request, profileImg));
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.PATCH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateUser(
        @PathVariable("userId") UUID userId,
        @RequestPart("userUpdateRequest") UserUpdateRequest request,
        @RequestPart(value = "profileImg", required = false) MultipartFile profileImg
        ) {
        return ResponseEntity.ok(userService.update(userId, request, profileImg));
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/api/user/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(value = "/api/users/{userId}/userStatus", method = RequestMethod.PATCH)
    public ResponseEntity<UserStatusResponse> updateUserStatus(@PathVariable("userId") UUID userId,
        @RequestBody UserStatusUpdateRequest request) {
        return ResponseEntity.ok(userStatusService.updateByUserId(userId, request));
    }

}
