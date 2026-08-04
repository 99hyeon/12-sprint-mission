package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtLogoutHandler implements LogoutHandler {

  private static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH_TOKEN";

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    ResponseCookie expiredRefreshToken = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
        .httpOnly(true)
        .secure(request.isSecure())
        .path("/")
        .sameSite("Strict")
        .maxAge(Duration.ZERO)
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, expiredRefreshToken.toString());
  }
}
