package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.auth.LoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.custom.BadRequestException;
import com.sprint.mission.discodeit.exception.custom.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.USER_EMAIL_NOT_FOUND.format(request.email()))
        );

        if(!user.getPassword().equals(request.password())){
            throw new BadRequestException(ErrorCode.WRONG_PASSWORD.getMessage());
        }

        return dtoFrom(user);
    }

    private LoginResponse dtoFrom(User user) {
        return new LoginResponse(
            user.getId(),
            user.getEmail(),
            user.getUserName()
        );
    }
}
