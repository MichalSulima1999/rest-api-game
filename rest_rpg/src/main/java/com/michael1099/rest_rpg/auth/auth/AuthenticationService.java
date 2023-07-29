package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.config.JwtService;
import com.michael1099.rest_rpg.auth.refreshToken.RefreshTokenService;
import com.michael1099.rest_rpg.auth.user.User;
import com.michael1099.rest_rpg.auth.user.UserRepository;
import com.michael1099.rest_rpg.exceptions.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public void register(@NotNull RegisterRequest request,
                         @NotNull String verificationURL) {
        assertAccountNotExists(request.getUsername(), request.getEmail());
        var user = User.of(request, passwordEncoder, UUID.randomUUID().toString());
        user = repository.save(user);

        sendVerificationEmail(user, verificationURL);
    }

    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request,
                                               HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);

        return createJwtResponse(user, response);
    }

    public void verify(String verificationCode) {
        User user = repository.findByVerificationCode(verificationCode)
                .orElseThrow(UserNotFoundException::new);

        if (user.isEnabled()) {
            throw new UserAlreadyVerifiedException();
        }
        user.setEnabled(true);
        repository.save(user);
    }

    private void assertAccountNotExists(@NotNull String username, @NotNull String email) {
        if (repository.findByUsername(username).isPresent()) {
            throw new AccountUsernameExistsException();
        }
        if (repository.findByEmail(email).isPresent()) {
            throw new AccountEmailExistsException();
        }
    }

    private AuthenticationResponse createJwtResponse(User user, HttpServletResponse response) {
        var jwtToken = jwtService.generateToken(user);

        sendRefreshToken(user, response);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }

    private void sendRefreshToken(@NotNull User user, HttpServletResponse response) {
        ResponseCookie springCookie = refreshTokenService.createRefreshToken(user.getUsername());
        response.setHeader(HttpHeaders.SET_COOKIE, springCookie.toString());
    }

    private void sendVerificationEmail(User user, String verificationURL) {
        String toAddress = user.getEmail();
        String fromAddress = "server@restrpg.com";
        String senderName = "RPG";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Rest RPG.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", user.getUsername());
            String verifyURL = verificationURL + "/auth/verify?code=" + user.getVerificationCode();

            content = content.replace("[[URL]]", verifyURL);

            helper.setText(content, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new VerificationEmailSendErrorException();
        }

        mailSender.send(message);
    }
}
