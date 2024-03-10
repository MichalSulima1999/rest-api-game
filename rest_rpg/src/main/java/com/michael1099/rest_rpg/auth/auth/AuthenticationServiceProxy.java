package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.user.Role;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// Tydzień 4, Proxy
// Stworzone zostało Proxy do serwisu
// Dzięki temu nie ma tutaj implementacji tego co jest w serwisie
// Możemy tutaj wykonywać jakieś działania, które chcemy żeby były wykonane przed rejestracją, logowaniem, weryfikacją maila
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceProxy implements AuthenticationService {

    private final AuthenticationService authenticationService;

    @Override
    public void register(@NotNull RegisterRequest request, @NotNull String verificationURL, @NotNull Role role) {
        log.info("User with username: {} and role {} is being registered", request.getUsername(), role);
        authenticationService.register(request, verificationURL, role);
    }

    @Override
    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request, HttpServletResponse response) {
        log.info("User with username: {} is being authenticated", request.getUsername());
        return authenticationService.authenticate(request, response);
    }

    @Override
    public void verify(String verificationCode) {
        log.info("User is being verified");
        authenticationService.verify(verificationCode);
    }
}
// Koniec Tydzień 4, Proxy
