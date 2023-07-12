package com.michael1099.rest_rpg.auth.refreshToken;

import com.michael1099.rest_rpg.auth.auth.AuthenticationResponse;
import com.michael1099.rest_rpg.auth.config.JwtService;
import com.michael1099.rest_rpg.auth.config.TokenProperties;
import com.michael1099.rest_rpg.auth.user.User;
import com.michael1099.rest_rpg.auth.user.UserRepository;
import com.michael1099.rest_rpg.exceptions.EmptyJwtException;
import com.michael1099.rest_rpg.exceptions.JwtExpiredException;
import com.michael1099.rest_rpg.exceptions.RefreshTokenNotFoundException;
import com.michael1099.rest_rpg.exceptions.UserNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenProperties tokenProperties;

    public AuthenticationResponse refreshToken(@NotEmpty String jwt) {
        if (jwt == null) {
            throw new EmptyJwtException();
        }
        RefreshToken refreshToken = refreshTokenRepository.findByToken(jwt).orElseThrow(RefreshTokenNotFoundException::new);
        verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);

        return new AuthenticationResponse(accessToken, user.getRole());
    }

    public ResponseCookie logout(@NotEmpty String jwt) {
        var refreshToken = refreshTokenRepository.findByToken(jwt).orElseThrow(RefreshTokenNotFoundException::new);

        refreshToken.setExpiryDate(Instant.now());
        refreshTokenRepository.save(refreshToken);

        return ResponseCookie
                .from(tokenProperties.getRefreshTokenCookieName(), "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(0)
                .build();
    }

    public ResponseCookie createRefreshToken(@NotEmpty String username) {
        var refreshToken = refreshTokenRepository.findByUser_Username(username).orElse(
                RefreshToken.builder().user(userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new)).build()
        );

        refreshToken.setExpiryDate(Instant.now().plusMillis(tokenProperties.getRefreshTokenExpirationMs()));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);

        return ResponseCookie.from(tokenProperties.getRefreshTokenCookieName(), refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(tokenProperties.getRefreshTokenExpirationMs() / 1000)
                .build();
    }

    private void verifyExpiration(@NotNull RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new JwtExpiredException();
        }
    }
}
