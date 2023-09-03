package com.michael1099.rest_rpg.auth.user;

import com.michael1099.rest_rpg.exceptions.UserNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String verificationCode);

    default @NotNull User getByUsername(@NotNull String username) {
        return findByUsername(username).orElseThrow(UserNotFoundException::new);
    }
}
