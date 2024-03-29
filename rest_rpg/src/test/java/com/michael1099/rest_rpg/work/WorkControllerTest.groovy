package com.michael1099.rest_rpg.work


import com.michael1099.rest_rpg.character.CharacterServiceHelper
import com.michael1099.rest_rpg.configuration.TestBase
import org.openapitools.model.ErrorCodes
import org.openapitools.model.ResourceType
import org.openapitools.model.WorkLite
import org.openapitools.model.WorkLitePage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import java.time.LocalDateTime

class WorkControllerTest extends TestBase {

    def baseUrl = "/work"
    def searchUrl = baseUrl + "/search"
    def startWorkUrl = { long workId, long characterId -> baseUrl + "/" + workId + "/start/" + characterId }
    def endWorkUrl = { long workId -> baseUrl + "/" + workId + "/end" }

    @Autowired
    WorkServiceHelper workServiceHelper

    @Autowired
    CharacterServiceHelper characterServiceHelper

    void cleanup() {
        characterServiceHelper.clean()
        workServiceHelper.clean()
    }

    def "should create work"() {
        given:
            def request = WorkHelper.createWorkCreateRequest()
        when:
            def response = httpPost(baseUrl, request, WorkLite, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            WorkHelper.compare(request, response.body)
            WorkHelper.compare(workServiceHelper.getWork(response.body.id), response.body)
    }

    def "should find works"() {
        given:
            workServiceHelper.saveWork(name: "Name")
            def work1 = workServiceHelper.saveWork(name: "Chop trees")
            def work2 = workServiceHelper.saveWork(name: "Chop more trees")
        and:
            def request = WorkHelper.createWorkSearchRequest(nameLike: "Chop")
        when:
            def response = httpPost(searchUrl, request, WorkLitePage, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.numberOfElements == 2
            WorkHelper.compare([work1, work2], response.body)
    }

    def "should start work"() {
        given:
            def work = workServiceHelper.saveWork()
            def character = characterServiceHelper.createCharacter(user)
        when:
            def response = httpGet(startWorkUrl(work.id, character.id), Void, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.NO_CONTENT
            characterServiceHelper.getCharacter(character.id).with {
                assert WorkHelper.compare(it.occupation.work, work)
                assert it.occupation.isOccupied()
            }
    }

    def "should not start work"() {
        given:
            def work = workServiceHelper.saveWork()
            def character = characterServiceHelper.createCharacter(user)
            character.occupation.setWork(work)
            character.occupation.setFinishTime(LocalDateTime.now().plusDays(1))
            character = characterServiceHelper.save(character)
        when:
            def response = httpGet(startWorkUrl(work.id, character.id), Void, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.CONFLICT
            response.errorMessage == ErrorCodes.CHARACTER_IS_OCCUPIED.toString()
    }

    def "should end work"() {
        given:
            def work = workServiceHelper.saveWork(resourceType: resourceType, resourceAmount: 100)
            def character = characterServiceHelper.createCharacter(user)
            character.occupation.setWork(work)
            character.occupation.setFinishTime(LocalDateTime.now().minusDays(1))
            character.equipment.setGold(100)
            character.equipment.setIron(100)
            character.equipment.setWood(100)
            character = characterServiceHelper.save(character)
        when:
            def response = httpGet(endWorkUrl(character.id), Void, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.NO_CONTENT
            characterServiceHelper.getCharacter(character.id).with {
                switch (resourceType) {
                    case ResourceType.GOLD:
                        assert it.equipment.gold == 200
                        break
                    case ResourceType.IRON:
                        assert it.equipment.iron == 200
                        break
                    case ResourceType.WOOD:
                        assert it.equipment.wood == 200
                        break
                }
                assert !it.occupation.isOccupied()
            }
        where:
            resourceType << [ResourceType.GOLD, ResourceType.IRON, ResourceType.WOOD]
    }

    def "should not end work"() {
        given:
            def work = workServiceHelper.saveWork()
            def character = characterServiceHelper.createCharacter(user)
            character.occupation.setWork(work)
            character.occupation.setFinishTime(LocalDateTime.now().plusDays(1))
            character = characterServiceHelper.save(character)
        when:
            def response = httpGet(endWorkUrl(character.id), Void, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.CONFLICT
            response.errorMessage == ErrorCodes.CHARACTER_STILL_WORKING.toString()
    }
}
