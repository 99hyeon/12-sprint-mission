package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;

@Tag(name = "Auth", description = "Auth API")
public interface AuthApi {

  @Operation(summary = "로그인")
  @ApiResponse(responseCode = "200", description = "로그인 성공")
  ResponseEntity<UserResponse> login(
      LoginRequest request
  );

  @Operation(summary = "CSRF 토큰 발급")
  @ApiResponse(responseCode = "203", description = "CSRF 토큰 발급 성공")
  ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken);

}
