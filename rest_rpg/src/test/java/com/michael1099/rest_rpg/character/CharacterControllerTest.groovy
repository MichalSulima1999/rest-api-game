package com.michael1099.rest_rpg.character

import com.michael1099.rest_rpg.auth.auth.AuthenticationServiceHelper
import com.michael1099.rest_rpg.configuration.TestBase
import com.michael1099.rest_rpg.statistics.StatisticsHelper
import org.openapitools.model.CharacterLite
import org.openapitools.model.ErrorCodes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.parsing.Problem
import org.springframework.http.HttpStatus

class CharacterControllerTest extends TestBase {

    def baseUrl = "/character"

    @Autowired
    AuthenticationServiceHelper authenticationServiceHelper

    @Autowired
    CharacterServiceHelper characterServiceHelper

    void cleanup() {
        characterServiceHelper.clean()
        authenticationServiceHelper.clean()
    }

    def "should create character"() {
        given:
            def user = authenticationServiceHelper.getUser()
            def accessToken = authenticationServiceHelper.generateAccessToken(user)
            def request = CharacterHelper.createCharacterCreateRequest()
        when:
            def response = httpPost(baseUrl, request, CharacterLite, [accessToken: accessToken])
        then:
            response.status == HttpStatus.OK
            CharacterHelper.compare(request, response.body)
    }

    def "should not create character"() {
        given:
            def user = authenticationServiceHelper.getUser()
            def accessToken = authenticationServiceHelper.generateAccessToken(user)
            characterServiceHelper.createCharacter(user, [name: "Carl"])
            def request = CharacterHelper.createCharacterCreateRequest(name: "Carl")
        when:
            def response = httpPost(baseUrl, request, Problem, [accessToken: accessToken])
        then:
            response.status == HttpStatus.CONFLICT
            response.errorMessage == ErrorCodes.CHARACTER_ALREADY_EXISTS.toString()
        when:
            request = CharacterHelper.createCharacterCreateRequest(
                    statistics: StatisticsHelper.createStatisticsUpdateRequest(strength: 100))
            response = httpPost(baseUrl, request, CharacterLite, [accessToken: accessToken])
        then:
            response.status == HttpStatus.FORBIDDEN
            response.errorMessage == ErrorCodes.NOT_ENOUGH_SKILL_POINTS.toString()
    }
}
