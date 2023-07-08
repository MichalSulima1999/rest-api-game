package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.config.JwtService;
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
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationResponse register(@NotNull RegisterRequest request,
                                           HttpServletResponse response) {
        var user = User.of(request, passwordEncoder);
        repository.save(user);

        return createJwtResponse(user, response);
    }

    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request,
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

    private AuthenticationResponse createJwtResponse(User user, HttpServletResponse response) {
        var jwtToken = jwtService.generateToken(user);

        sendRefreshToken(user, response);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }

    private void sendRefreshToken(@NotNull User user, HttpServletResponse response) {
        ResponseCookie springCookie = refreshTokenService.createRefreshToken(user.getUsername());
        response.setHeader(HttpHeaders.SET_COOKIE, springCookie.toString());
    }
}
