package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.function.Supplier;

import org.springframework.security.web.csrf.*;
import org.springframework.util.StringUtils;

public class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {

    // 토큰 원문 그대로 사용
    private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
    // 토큰 XOR로 마스킹해 응답에 노출
    private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

    /**
     * 요청 처리 중 CSRF 토큰을 준비할 때 호출됨
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Supplier<CsrfToken> csrfToken
    ) {
        xor.handle(request, response, csrfToken);
        csrfToken.get();
    }

    /**
     * 서버가 요청의 CSRF 토큰을 검증할 때 호출됨
     */
    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        String headerValue = request.getHeader(csrfToken.getHeaderName());
        return (StringUtils.hasText(headerValue) ? plain : xor)
                .resolveCsrfTokenValue(request, csrfToken);
    }
}
