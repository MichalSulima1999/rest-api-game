package com.michael1099.rest_rpg.character

import com.michael1099.rest_rpg.character.model.CharacterArtwork
import com.michael1099.rest_rpg.configuration.TestBase
import com.michael1099.rest_rpg.fight.model.Fight
import com.michael1099.rest_rpg.occupation.Occupation
import com.michael1099.rest_rpg.statistics.StatisticsHelper
import com.michael1099.rest_rpg.statistics.StatisticsServiceHelper
import org.openapitools.model.CharacterBasics
import org.openapitools.model.CharacterDetails
import org.openapitools.model.CharacterLite
import org.openapitools.model.CharacterSex
import org.openapitools.model.ErrorCodes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.parsing.Problem
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

import java.util.stream.Collectors

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

// Tydzień 13 - testy
// Zostały stworzone klasy testowe do kontrolerów.
// Przykładowo przy tworzeniu postaci porównywana jest tworzona postać ze zwracaną postacią.
// W niektórych przypadkach tworzone są też testy, które sprawdzają wyrzucanie wyjątków.
class CharacterControllerTest extends TestBase {

    def baseUrl = "/character"
    def imageUrl = { String characterArtwork -> baseUrl + "/image/" + characterArtwork }
    def thumbnailUrl = { String characterArtwork -> baseUrl + "/thumbnail/" + characterArtwork }
    def artworksUrl = baseUrl + "/artworks"
    def userCharactersUrl = baseUrl + "/user-characters"
    def userCharacterUrl = { long characterId -> baseUrl + "/" + characterId }

    @Autowired
    CharacterServiceHelper characterServiceHelper

    @Autowired
    StatisticsServiceHelper statisticsServiceHelper

    void cleanup() {
        statisticsServiceHelper.clean()
        characterServiceHelper.clean()
    }

    def "should create character"() {
        given:
            def request = CharacterHelper.createCharacterCreateRequest()
        when:
            def response = httpPost(baseUrl, request, CharacterLite, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            CharacterHelper.compare(request, response.body)
            def statistics = statisticsServiceHelper.getCharacterStatistics(response.body.id)
            StatisticsHelper.compare(statistics, request.statistics, response.body.race)
    }

    def "should not create character"() {
        given:
            characterServiceHelper.createCharacter(user, [name: "Carl"])
            def request = CharacterHelper.createCharacterCreateRequest(name: "Carl")
        when:
            def response = httpPost(baseUrl, request, Problem, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.CONFLICT
            response.errorMessage == ErrorCodes.CHARACTER_ALREADY_EXISTS.toString()
        when:
            request = CharacterHelper.createCharacterCreateRequest(
                    statistics: StatisticsHelper.createStatisticsUpdateRequest(strength: 100))
            response = httpPost(baseUrl, request, CharacterLite, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.FORBIDDEN
            response.errorMessage == ErrorCodes.NOT_ENOUGH_SKILL_POINTS.toString()
        when:
            request = CharacterHelper.createCharacterCreateRequest(characterClass: "Demon")
            response = httpPost(baseUrl, request, CharacterLite, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.NOT_FOUND
            response.errorMessage == ErrorCodes.ENUM_VALUE_NOT_FOUND.toString()
    }

    def "should get character image"() {
        when:
            def requestGet = get(imageUrl(artwork))
                    .contentType(MediaType.IMAGE_JPEG)
                    .accept(MediaType.IMAGE_JPEG)

            def response = mvc.perform(requestGet).andReturn().response
        then:
            response.status == httpStatus
        where:
            artwork                                    || httpStatus
            CharacterArtwork.HUMAN_FEMALE_1.toString() || HttpStatus.OK.value()
            "FEMALE_123"                               || HttpStatus.NOT_FOUND.value()
    }

    def "should get character thumbnail image"() {
        when:
            def requestGet = get(thumbnailUrl(artwork))
                    .contentType(MediaType.IMAGE_JPEG)
                    .accept(MediaType.IMAGE_JPEG)

            def response = mvc.perform(requestGet).andReturn().response
        then:
            response.status == httpStatus
        where:
            artwork                                    || httpStatus
            CharacterArtwork.HUMAN_FEMALE_1.toString() || HttpStatus.OK.value()
            "FEMALE_123"                               || HttpStatus.NOT_FOUND.value()
    }

    def "should get character thumbnail enum"() {
        when:
            def response = httpGet(artworksUrl, List<String>, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.size() == Arrays.stream(CharacterArtwork.values()).map(Objects::toString).collect(Collectors.toList()).size()
    }

    def "should get user characters"() {
        when:
            def user2 = authenticationServiceHelper.createUser()
            def character1 = characterServiceHelper.createCharacter(user, [name: "Carl"])
            // Tydzień 2, Wzorzec Prototype
            // Tworzona jest nowa postać na podstawie poprzedniej, a następnie zmieniane są tylko wymagane cechy
            def character2 = character1.clone()
            character2.name = "Johnny"
            character2.sex = CharacterSex.MALE
            character2 = characterServiceHelper.save(character2)
            // Koniec, Tydzień 2, Wzorzec Prototype
            def anotherUserCharacter = characterServiceHelper.createCharacter(user2, [name: "Fred", sex: CharacterSex.MALE])
            def list = [character1, character2]
            def response = httpGet(userCharactersUrl, CharacterBasics, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            CharacterHelper.compare(list, response.body.content)
    }

    def "should get user character"() {
        when:
            def character = characterServiceHelper.createCharacter(user, [name: "Carl", occupation: Occupation.builder().fight(Fight.builder().isActive(true).build()).build()])
            def response = httpGet(userCharacterUrl(character.getId()), CharacterDetails, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            CharacterHelper.compare(character, response.body)
    }

    def "should not get another user character"() {
        when:
            def user2 = authenticationServiceHelper.createUser()
            def character = characterServiceHelper.createCharacter(user2, [name: "Carl"])
            def response = httpGet(userCharacterUrl(character.getId()), Problem, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.NOT_FOUND
            response.errorMessage == ErrorCodes.CHARACTER_NOT_FOUND.toString()
    }
}
// Koniec Tydzień 13 - testy
