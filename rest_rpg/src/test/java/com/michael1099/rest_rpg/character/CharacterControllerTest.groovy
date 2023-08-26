package com.michael1099.rest_rpg.character


import com.michael1099.rest_rpg.character.model.CharacterArtwork
import com.michael1099.rest_rpg.configuration.TestBase
import com.michael1099.rest_rpg.statistics.StatisticsHelper
import org.openapitools.model.CharacterLite
import org.openapitools.model.ErrorCodes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.parsing.Problem
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

import java.util.stream.Collectors

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class CharacterControllerTest extends TestBase {

    def baseUrl = "/character"
    def imageUrl = { String characterArtwork -> baseUrl + "/image/" + characterArtwork }
    def thumbnailUrl = { String characterArtwork -> baseUrl + "/thumbnail/" + characterArtwork }
    def artworksUrl = baseUrl + "/artworks"

    @Autowired
    CharacterServiceHelper characterServiceHelper

    void cleanup() {
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
}
