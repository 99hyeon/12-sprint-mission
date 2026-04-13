package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.auth.LoginResponse;
import com.sprint.mission.discodeit.entity.User;
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
            () -> new IllegalArgumentException("해당 이메일의 유저 존재 안 함")
        );

        if(!user.getPassword().equals(request.password())){
            throw new IllegalArgumentException("비밀번호 틀림");
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
