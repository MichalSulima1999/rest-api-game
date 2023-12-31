package com.michael1099.rest_rpg.auth.config;

import com.michael1099.rest_rpg.auth.user.User;
import com.michael1099.rest_rpg.auth.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${default-admin.username}")
    private String adminUsername;

    @Value("${default-admin.email}")
    private String adminEmail;

    @Value("${default-admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        createDefaultUser();
    }

    private void createDefaultUser() {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            userRepository.save(User.createDefaultAdmin(
                    adminUsername, adminEmail, passwordEncoder.encode(adminPassword)));
        }
    }
}
