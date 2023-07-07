package com.michael1099.rest_rpg.auth.refreshToken;

import com.michael1099.rest_rpg.auth.config.TokenProperties;
import com.michael1099.rest_rpg.auth.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenProvider {

    private final RefreshTokenService refreshTokenService;
    private final TokenProperties tokenProperties;

    public ResponseCookie createRefreshTokenCookie(User user) {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return ResponseCookie.from(tokenProperties.getRefreshTokenCookieName(), refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(tokenProperties.getRefreshTokenExpirationMs() / 1000)
                .build();
    }
}
