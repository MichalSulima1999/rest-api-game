package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.user.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @NotNull
    private String username;

    @NotNull
    private String token;

    @NotNull
    private Role role;
}
