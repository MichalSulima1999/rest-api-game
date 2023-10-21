package com.michael1099.rest_rpg.auth.auth

import com.michael1099.rest_rpg.configuration.TestBase
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.Rollback
import org.springframework.util.LinkedMultiValueMap
import spock.lang.Unroll

class AuthenticationControllerTest extends TestBase {

    def baseUrl = "/auth"
    def registerUrl = baseUrl + "/register"
    def authenticateUrl = baseUrl + "/authenticate"
    def verifyUrl = baseUrl + "/verify"

    @Autowired
    AuthenticationServiceHelper authenticationServiceHelper

    void cleanup() {
        authenticationServiceHelper.clean()
    }

    def "should register user"() {
        when:
            def request = RegisterRequest.builder()
                    .username("Michael1099")
                    .email("michael1099@gmail.com")
                    .password("12345678")
                    .build()
            def response = httpPost(registerUrl, request, Void)
        then:
            mailSender.send(_ as MimeMessage) >> {}// TODO: body
            response.status == HttpStatus.OK
    }

    def "should login user"() {
        given:
            def user = authenticationServiceHelper.createUser()
        when:
            def request = AuthenticationRequest.builder()
                    .username(user.username)
                    .password("12345678")
                    .build()
            def response = httpPost(authenticateUrl, request, AuthenticationResponse)
        then:
            response.status == HttpStatus.OK
            response.body.role == user.role
            response.body.username == user.username
            response.body.token
    }

    @Unroll
    def "should verify not verified user"() {
        given:
            def user = authenticationServiceHelper.createUser(enabled: enabled)
        when:
            def params = new LinkedMultiValueMap()
            params.add("code", user.verificationCode)
            def response = httpGet(verifyUrl, Void, [parameters: params])
        then:
            response.status == httpStatus
        where:
            enabled || httpStatus
            false   || HttpStatus.OK
            true    || HttpStatus.FORBIDDEN
    }
}
