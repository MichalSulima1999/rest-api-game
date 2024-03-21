package com.michael1099.rest_rpg.auth.auth;

import com.michael1099.rest_rpg.auth.config.JwtService;
import com.michael1099.rest_rpg.auth.refreshToken.RefreshTokenService;
import com.michael1099.rest_rpg.auth.user.Role;
import com.michael1099.rest_rpg.auth.user.User;
import com.michael1099.rest_rpg.auth.user.UserRepository;
import com.michael1099.rest_rpg.exceptions.AccountEmailExistsException;
import com.michael1099.rest_rpg.exceptions.AccountUsernameExistsException;
import com.michael1099.rest_rpg.exceptions.UserAlreadyVerifiedException;
import com.michael1099.rest_rpg.exceptions.UserNotFoundException;
import com.michael1099.rest_rpg.exceptions.VerificationEmailSendErrorException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;


    public void register(@NotNull RegisterRequest request,
                         @NotNull String verificationURL,
                         @NotNull Role role) {
        var user = User.of(request, passwordEncoder, role);
        user = repository.save(user);

        sendVerificationEmail(user, verificationURL);
    }

    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request,
                                               HttpServletResponse response) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);

        return createJwtResponse(user, response);
    }

    public void verify(String verificationCode) {
        User user = repository.findByVerificationCode(verificationCode)
                .orElseThrow(UserNotFoundException::new);
        user.setEnabled(true);
        repository.save(user);
    }

    private AuthenticationResponse createJwtResponse(User user, HttpServletResponse response) {
        var jwtToken = jwtService.generateToken(user);

        sendRefreshToken(user, response);

        return AuthenticationResponse.builder()
                .username(user.getUsername())
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
