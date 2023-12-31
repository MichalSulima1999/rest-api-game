package com.michael1099.rest_rpg.auth.user;

import com.michael1099.rest_rpg.auth.auth.RegisterRequest;
import com.michael1099.rest_rpg.auth.refreshToken.RefreshToken;
import com.michael1099.rest_rpg.character.model.Character;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String username;

    @Column(unique = true)
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private boolean enabled;

    @NotBlank
    @Size(max = 64)
    private String verificationCode;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "user")
    private Set<Character> characters;

    public static User of(RegisterRequest request,
                          PasswordEncoder passwordEncoder,
                          @NotNull Role role) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();
    }

    public static User createDefaultAdmin(@NotBlank String username,
                                          @NotBlank String email,
                                          @NotBlank String encodedPassword) {
        return User.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(Role.ADMIN)
                .enabled(true)
                .verificationCode(UUID.randomUUID().toString())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
