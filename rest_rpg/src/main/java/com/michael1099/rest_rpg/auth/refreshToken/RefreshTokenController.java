package com.michael1099.rest_rpg.auth.refreshToken;

import com.michael1099.rest_rpg.auth.auth.AuthenticationResponse;
import com.michael1099.rest_rpg.auth.config.JwtService;
import com.michael1099.rest_rpg.auth.config.TokenProperties;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/refresh-token")
@Slf4j
public record RefreshTokenController(

        RefreshTokenService refreshTokenService,
        JwtService jwtService,
        TokenProperties tokenProperties
) {

    @GetMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@NotEmpty @CookieValue(name = "jwt") String jwt) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(jwt));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletResponse response, @NotEmpty @CookieValue(name = "jwt") String jwt) {
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenService.logout(jwt).toString());
        return ResponseEntity.ok().build();
    }
}
