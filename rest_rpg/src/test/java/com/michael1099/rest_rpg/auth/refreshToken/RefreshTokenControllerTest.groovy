package com.michael1099.rest_rpg.auth.refreshToken

import com.michael1099.rest_rpg.auth.auth.AuthenticationResponse
import com.michael1099.rest_rpg.auth.auth.AuthenticationServiceHelper
import com.michael1099.rest_rpg.configuration.TestBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class RefreshTokenControllerTest extends TestBase {

    def baseUrl = "/refresh-token"
    def refreshUrl = baseUrl + "/refresh"
    def logoutUrl = baseUrl + "/logout"

    @Autowired
    AuthenticationServiceHelper authenticationServiceHelper

    void cleanup() {
        authenticationServiceHelper.clean()
    }

    def "should refresh token"() {
        given:
            def user = authenticationServiceHelper.createUser()
        when:
            def response = httpGet(refreshUrl, AuthenticationResponse, [refreshToken: user.refreshToken.token])
        then:
            response.status == HttpStatus.OK
    }

    def "should logout user"() {
        given:
            def user = authenticationServiceHelper.createUser()
        when:
            def response = httpGet(logoutUrl, AuthenticationResponse, [refreshToken: user.refreshToken.token])
        then:
            response.status == HttpStatus.OK
    }
}
