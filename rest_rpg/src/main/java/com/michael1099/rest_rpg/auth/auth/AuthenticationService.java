package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.config.JwtService;
import com.michael1099.rest_rpg.auth.refreshToken.RefreshTokenProvider;
import com.michael1099.rest_rpg.auth.refreshToken.RefreshTokenService;
import com.michael1099.rest_rpg.auth.user.User;
import com.michael1099.rest_rpg.auth.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenProvider refreshTokenProvider;

    public AuthenticationResponse register(RegisterRequest request,
                                           HttpServletResponse response) {
        var user = User.of(request, passwordEncoder);
        repository.save(user);

        return createJwtResponse(user, response);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request,
                                               HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();

        return createJwtResponse(user, response);
    }

    private void sendRefreshToken(User user, HttpServletResponse response) {
        refreshTokenService.deleteByUserUsername(user.getUsername());
        ResponseCookie springCookie = refreshTokenProvider.createRefreshTokenCookie(user);
        response.setHeader(HttpHeaders.SET_COOKIE, springCookie.toString());
    }

    private AuthenticationResponse createJwtResponse(User user, HttpServletResponse response) {
        var jwtToken = jwtService.generateToken(user);

        sendRefreshToken(user, response);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }
}
