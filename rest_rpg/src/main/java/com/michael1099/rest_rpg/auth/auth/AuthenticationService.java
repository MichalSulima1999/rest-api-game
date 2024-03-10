package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.user.Role;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

public interface AuthenticationService {

    void register(@NotNull RegisterRequest request,
                  @NotNull String verificationURL,
                  @NotNull Role role);

    AuthenticationResponse authenticate(@NotNull AuthenticationRequest request,
                                        HttpServletResponse response);

    void verify(String verificationCode);
}
