package com.michael1099.rest_rpg.auth.refreshToken;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(@NotEmpty String token);

    Optional<RefreshToken> findByUser_Username(@NotEmpty String username);
}
