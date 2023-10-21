package com.michael1099.rest_rpg.skill

import com.michael1099.rest_rpg.configuration.TestBase
import org.openapitools.model.ErrorCodes
import org.openapitools.model.SkillLite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class SkillControllerTest extends TestBase {

    def baseUrl = "/skill"

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
}
