package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import java.util.UUID;

public record UserUpdateRequest(
    UUID id,
    String email,
    String nickName,
    String userName,
    BinaryContentCreateRequest profileImage
) { }
