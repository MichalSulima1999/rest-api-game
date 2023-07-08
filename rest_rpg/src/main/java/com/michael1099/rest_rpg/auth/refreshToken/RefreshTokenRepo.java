package com.michael1099.rest_rpg.auth.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(@NotNull String token);

    Optional<RefreshToken> findByUser_Username(@NotNull String username);
}
