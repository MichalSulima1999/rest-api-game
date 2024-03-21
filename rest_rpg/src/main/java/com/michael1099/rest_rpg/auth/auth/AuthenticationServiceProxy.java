package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.user.Role;
import com.michael1099.rest_rpg.auth.user.User;
import com.michael1099.rest_rpg.auth.user.UserRepository;
import com.michael1099.rest_rpg.exceptions.AccountEmailExistsException;
import com.michael1099.rest_rpg.exceptions.AccountUsernameExistsException;
import com.michael1099.rest_rpg.exceptions.UserAlreadyVerifiedException;
import com.michael1099.rest_rpg.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

// Tydzień 4, Proxy
// Stworzone zostało Proxy do serwisu
// Dzięki temu nie ma tutaj implementacji tego co jest w serwisie
// Możemy tutaj wykonywać jakieś działania, które chcemy żeby były wykonane przed rejestracją, logowaniem, weryfikacją maila
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceProxy implements AuthenticationService {

    private final AuthenticationServiceImpl authenticationServiceImpl;
    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(@NotNull RegisterRequest request, @NotNull String verificationURL, @NotNull Role role) {
        String username = request.getUsername();
        String email = request.getEmail();
        if (repository.findByUsername(username).isPresent()) {
            throw new AccountUsernameExistsException();
        }
        if (repository.findByEmail(email).isPresent()) {
            throw new AccountEmailExistsException();
        }
        authenticationServiceImpl.register(request, verificationURL, role);
    }

    @Override
    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    ));
        } catch (AuthenticationException exception) {
            throw new UserNotFoundException();
        }
        return authenticationServiceImpl.authenticate(request, response);
    }

    @Override
    public void verify(String verificationCode) {
        User user = repository.findByVerificationCode(verificationCode)
                .orElseThrow(UserNotFoundException::new);

        if (user.isEnabled()) {
            throw new UserAlreadyVerifiedException();
        }
        authenticationServiceImpl.verify(verificationCode);
    }
}
// Koniec Tydzień 4, Proxy
