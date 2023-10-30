package com.michael1099.rest_rpg.adventure

import com.michael1099.rest_rpg.configuration.TestBase
import com.michael1099.rest_rpg.enemy.EnemyServiceHelper
import com.michael1099.rest_rpg.skill.SkillHelper
import org.openapitools.model.AdventureLite
import org.openapitools.model.SkillBasicPage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class AdventureControllerTest extends TestBase {

    def baseUrl = "/adventure"

    @Autowired
    EnemyServiceHelper enemyServiceHelper

    @Autowired
    AdventureServiceHelper adventureServiceHelper

    void cleanup() {
        adventureServiceHelper.clean()
        enemyServiceHelper.clean()
    }

    def "should create adventure"() {
        given:
            def enemy = enemyServiceHelper.saveEnemy()
        and:
            def request = AdventureHelper.createAdventureCreateRequest(enemy)
        when:
            def response = httpPost(baseUrl, request, AdventureLite, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            AdventureHelper.compare(request, response.body)
    }

    def "should find adventures"() {
        given:
            adventureServiceHelper.saveAdventure(name: "Kill troll")
            def adventure1 = adventureServiceHelper.saveAdventure(name: "Adventure 1")
            def adventure2 = adventureServiceHelper.saveAdventure(name: "Adventure 2")
        when:
            def response = httpPost(searchUrl, request, SkillBasicPage, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.numberOfElements == 2
            SkillHelper.compare([skill, skill2], response.body)
    }
}
