package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty
    private String username;

    @Email
    @NotEmpty
    private String email;

    @Length(min = 8)
    @NotEmpty
    private String password;

    @NotNull
    private Role role;
}
