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
    def statisticUrl = { long characterId -> baseUrl + "/" + characterId }
    def trainUrl = { long characterId -> baseUrl + "/" + characterId + "/train" }

    @Autowired
    CharacterServiceHelper characterServiceHelper

    void cleanup() {
        characterServiceHelper.clean()
    }

    def "should get character statistics"() {
        when:
            def character = characterServiceHelper.createCharacter(user, [name: "Carl"])
            def response = httpGet(statisticUrl(character.getId()), StatisticsDetails, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            StatisticsHelper.compare(character.statistics, response.body)
    }

    def "should not get character statistics"() {
        when:
            def user2 = authenticationServiceHelper.createUser()
            def character = characterServiceHelper.createCharacter(user2, [name: "Carl"])
            def response = httpGet(statisticUrl(character.getId()), Problem, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.NOT_FOUND
            response.errorMessage == ErrorCodes.CHARACTER_NOT_FOUND.toString()
    }

    def "should train character statistics"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [
                    name      : "Carl",
                    statistics: StatisticsHelper.statistics(freeStatisticPoints: 50, strength: 10),
            ])
            def request = StatisticsHelper.createStatisticsUpdateRequest(
                    strength: 10,
                    dexterity: 10,
                    constitution: 10,
                    intelligence: 10)
        when:
            def response = httpPost(trainUrl(character.getId()), request, StatisticsDetails, [accessToken: userAccessToken])
            character = characterServiceHelper.getCharacter(character.id)
        then:
            response.status == HttpStatus.OK
            StatisticsHelper.compare(character.statistics, response.body)
            character.statistics.strength == 20
    }

    def "should not train character statistics"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [
                    name      : "Carl",
                    statistics: StatisticsHelper.statistics(freeStatisticPoints: 39, strength: 10),
            ])
            def request = StatisticsHelper.createStatisticsUpdateRequest(
                    strength: 10,
                    dexterity: 10,
                    constitution: 10,
                    intelligence: 10)
        when:
            def response = httpPost(trainUrl(character.getId()), request, StatisticsDetails, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.FORBIDDEN
            response.errorMessage == ErrorCodes.NOT_ENOUGH_SKILL_POINTS.toString()
    }
}
