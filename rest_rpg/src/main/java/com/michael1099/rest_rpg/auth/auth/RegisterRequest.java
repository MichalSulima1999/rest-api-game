package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;

    @Email
    private String email;

    @Min(8)
    private String password;
    
    private Role role;
}
