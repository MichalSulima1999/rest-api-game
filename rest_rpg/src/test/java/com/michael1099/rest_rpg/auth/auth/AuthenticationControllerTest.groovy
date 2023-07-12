package com.michael1099.rest_rpg.auth.auth

import com.michael1099.rest_rpg.auth.user.Role
import com.michael1099.rest_rpg.auth.user.User
import com.michael1099.rest_rpg.auth.user.UserRepository
import com.michael1099.rest_rpg.configuration.TestBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder

class AuthenticationControllerTest extends TestBase {

    def baseUrl = "/api/auth"
    def registerUrl = baseUrl + "/register"
    def authenticateUrl = baseUrl + "/authenticate"

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    UserRepository userRepository

    void cleanup() {
        userRepository.deleteAll()
    }

    def "should register user"() {
        when:
            def request = RegisterRequest.builder()
                    .username("Michael1099")
                    .email("michael1099@gmail.com")
                    .role(Role.ADMIN)
                    .password("12345678")
                    .build()
            def response = httpPost(registerUrl, request, AuthenticationResponse)
        then:
            response.status == HttpStatus.OK
            response.body.role == request.role
            response.body.token
    }

    def "Authenticate"() {
        given:
            def user = User.builder()
                    .username("Michael1099")
                    .email("michael1099@gmail.com")
                    .password(passwordEncoder.encode("12345678"))
                    .role(Role.USER)
                    .build()
            userRepository.save(user);
        when:
            def request = AuthenticationRequest.builder()
                    .username("Michael1099")
                    .password("12345678")
                    .build()
            def response = httpPost(authenticateUrl, request, AuthenticationResponse)
        then:
            response.status == HttpStatus.OK
            response.body.role == user.role
            response.body.token
    }
}
