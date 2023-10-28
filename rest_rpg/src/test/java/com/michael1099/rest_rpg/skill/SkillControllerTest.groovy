package com.michael1099.rest_rpg.skill

import com.michael1099.rest_rpg.configuration.TestBase
import org.openapitools.model.ErrorCodes
import org.openapitools.model.SkillBasicPage
import org.openapitools.model.SkillDetails
import org.openapitools.model.SkillLite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class SkillControllerTest extends TestBase {

    def baseUrl = "/skill"
    def searchUrl = baseUrl + "/search"
    def skillUrl = { long skillId -> baseUrl + "/" + skillId }

    @Autowired
    SkillServiceHelper skillServiceHelper

    void cleanup() {
        skillServiceHelper.clean()
    }

    def "should create skill"() {
        given:
            def request = SkillHelper.createSkillCreateRequest()
        when:
            def response = httpPost(baseUrl, request, SkillLite, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            SkillHelper.compare(request, response.body)
    }

    def "should not create skill"() {
        given:
            skillServiceHelper.createSkill(name: "Skill")
            def request = SkillHelper.createSkillCreateRequest(name: "Skill")
        when:
            def response = httpPost(baseUrl, request, SkillLite, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.CONFLICT
            response.errorMessage == ErrorCodes.SKILL_ALREADY_EXISTS.toString()
    }

    def "should find skills"() {
        given:
            skillServiceHelper.createSkill(name: "Fireball")
            def skill = skillServiceHelper.createSkill(name: "Skill")
            def skill2 = skillServiceHelper.createSkill(name: "Skill2")
            def request = SkillHelper.createSkillSearchRequest(name: "Skill")
        when:
            def response = httpPost(searchUrl, request, SkillBasicPage, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.numberOfElements == 2
            SkillHelper.compare([skill, skill2], response.body)
    }

    def "should get skill"() {
        given:
            skillServiceHelper.createSkill(name: "Fireball")
            def skill = skillServiceHelper.createSkill(name: "Skill")
        when:
            def response = httpGet(skillUrl(skill.id), SkillDetails, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            SkillHelper.compare(skill, response.body)
    }
}
