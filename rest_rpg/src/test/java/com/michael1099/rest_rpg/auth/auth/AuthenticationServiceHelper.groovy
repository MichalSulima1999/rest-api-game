package com.michael1099.rest_rpg.auth.auth

import com.michael1099.rest_rpg.auth.config.TokenProperties
import com.michael1099.rest_rpg.auth.refreshToken.RefreshToken
import com.michael1099.rest_rpg.auth.refreshToken.RefreshTokenRepo
import com.michael1099.rest_rpg.auth.user.Role
import com.michael1099.rest_rpg.auth.user.User
import com.michael1099.rest_rpg.auth.user.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import java.security.Key
import java.time.Instant

@Service
class AuthenticationServiceHelper {

    @Autowired
    UserRepository userRepository

    @Autowired
    RefreshTokenRepo refreshTokenRepo

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    TokenProperties tokenProperties

    def clean() {
        userRepository.deleteAll()
        refreshTokenRepo.deleteAll()
    }

    User getUser(Map customArgs = [:]) {
        Map args = [
                username        : "User",
                email           : "user@gmail.com",
                password        : "12345678",
                verificationCode: "123123123",
                role            : Role.USER,
                enabled         : true
        ]
        args << customArgs

        def user = User.builder()
                .username(args.username)
                .email(args.email)
                .password(passwordEncoder.encode(args.password))
                .verificationCode(args.verificationCode)
                .role(args.role)
                .enabled(args.enabled)
                .build()
        user = userRepository.save(user)

        def refreshToken = refreshTokenRepo.save(new RefreshToken(null, user, UUID.randomUUID().toString(), Instant.now().plusMillis(100000)))
        user.setRefreshToken(refreshToken)
        return user
    }

    String generateAccessToken(UserDetails userDetails) {
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getAccessTokenExpirationMs()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact()
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
