package com.michael1099.rest_rpg.auth.refreshToken;

import com.michael1099.rest_rpg.auth.auth.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refresh-token")
@RequiredArgsConstructor
@Validated
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@CookieValue(name = "jwt") String jwt) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(jwt));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletResponse response, @CookieValue(name = "jwt") String jwt) {
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenService.logout(jwt).toString());
        return ResponseEntity.ok().build();
    }
}
