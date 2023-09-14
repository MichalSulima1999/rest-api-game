package com.michael1099.rest_rpg.statistics

import com.michael1099.rest_rpg.character.CharacterServiceHelper
import com.michael1099.rest_rpg.configuration.TestBase
import org.openapitools.model.ErrorCodes
import org.openapitools.model.StatisticsDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.parsing.Problem
import org.springframework.http.HttpStatus

class StatisticsControllerTest extends TestBase {

    def baseUrl = "/statistics"
    def getStatisticsUrl = { long characterId -> baseUrl + "/" + characterId }

    @Autowired
    CharacterServiceHelper characterServiceHelper

    void cleanup() {
        characterServiceHelper.clean()
    }

    def "should get character statistics"() {
        when:
            def character = characterServiceHelper.createCharacter(user, [name: "Carl"])
            def response = httpGet(getStatisticsUrl(character.getId()), StatisticsDetails, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            StatisticsHelper.compare(character.statistics, response.body)
    }

    def "should not get character statistics"() {
        when:
            def user2 = authenticationServiceHelper.createUser()
            def character = characterServiceHelper.createCharacter(user2, [name: "Carl"])
            def response = httpGet(getStatisticsUrl(character.getId()), Problem, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.NOT_FOUND
            response.errorMessage == ErrorCodes.CHARACTER_NOT_FOUND.toString()
    }
}
