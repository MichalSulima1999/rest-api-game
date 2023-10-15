package com.michael1099.rest_rpg.enemy

import com.michael1099.rest_rpg.configuration.TestBase
import com.michael1099.rest_rpg.skill.SkillServiceHelper
import org.openapitools.model.ElementAction
import org.openapitools.model.ElementEvent
import org.openapitools.model.EnemyLite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import java.util.stream.Collectors

class EnemyControllerTest extends TestBase {

    def baseUrl = "/enemy"
    def strategyEventUrl = baseUrl + "/strategy-event"
    def strategyActionUrl = baseUrl + "/strategy-actions"

    @Autowired
    SkillServiceHelper skillServiceHelper

    @Autowired
    EnemyServiceHelper enemyServiceHelper

    void cleanup() {
        enemyServiceHelper.clean()
        skillServiceHelper.clean()
    }

    def "should get strategy element event"() {
        when:
            def response = httpGet(strategyEventUrl, List<String>, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.size() == Arrays.stream(ElementEvent.values()).map(Objects::toString).collect(Collectors.toList()).size()
    }

    def "should get strategy element action"() {
        when:
            def response = httpGet(strategyActionUrl, List<String>, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.size() == Arrays.stream(ElementAction.values()).map(Objects::toString).collect(Collectors.toList()).size()
    }

    def "should create enemy"() {
        given:
            def skill = skillServiceHelper.createSkill()
            def strategyRequests = [
                    EnemyHelper.createStrategyElementCreateRequest(
                            event: ElementEvent.ENEMY_HEALTH_20_40,
                            action: ElementAction.USE_POTION),
                    EnemyHelper.createStrategyElementCreateRequest(
                            event: ElementEvent.ENEMY_HEALTH_60_80,
                            action: ElementAction.NORMAL_ATTACK)
            ]
            def request = EnemyHelper.createEnemyCreateRequest(skill, [
                    name           : "Boar",
                    hp             : 100,
                    mana           : 100,
                    damage         : 30,
                    numberOfPotions: 3,
                    skillId        : 2,
                    enemyStrategy  : strategyRequests])
        when:
            def response = httpPost(baseUrl, request, EnemyLite, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            EnemyHelper.compare(request, response.body)
    }
}
