package com.michael1099.rest_rpg.skill

import com.michael1099.rest_rpg.character.CharacterServiceHelper
import com.michael1099.rest_rpg.configuration.TestBase
import com.michael1099.rest_rpg.helpers.DeleteServiceHelper
import org.openapitools.model.CharacterSkillBasics
import org.openapitools.model.ErrorCodes
import org.openapitools.model.SkillBasicPage
import org.openapitools.model.SkillDetails
import org.openapitools.model.SkillLite
import org.openapitools.model.SkillLites
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class SkillControllerTest extends TestBase {

    def baseUrl = "/skill"
    def searchUrl = baseUrl + "/search"
    def skillUrl = { long skillId -> baseUrl + "/" + skillId }
    def characterSkillsUrl = { long characterId -> baseUrl + "/character/" + characterId }
    def skillLearnUrl = { long skillId, long characterId -> baseUrl + "/" + skillId + "/learn/" + characterId }

    @Autowired
    SkillServiceHelper skillServiceHelper

    @Autowired
    CharacterServiceHelper characterServiceHelper

    @Autowired
    DeleteServiceHelper deleteServiceHelper

    void cleanup() {
        deleteServiceHelper.clean()
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

    def "should get skills"() {
        given:
            def skill1 = skillServiceHelper.createSkill(name: "Fireball")
            def skill2 = skillServiceHelper.createSkill(name: "Skill")
            skillServiceHelper.createSkill(name: "Skill 2", deleted: true)
        when:
            def response = httpGet(baseUrl, SkillLites, [accessToken: adminAccessToken])
        then:
            response.status == HttpStatus.OK
            SkillHelper.compare([skill1, skill2], response.body)
    }

    def "should get character skills"() {
        given:
            def skill1 = skillServiceHelper.createSkill(name: "Fireball")
            def skill2 = skillServiceHelper.createSkill(name: "Skill")
            skillServiceHelper.createSkill(name: "Dash")
            def character = characterServiceHelper.createCharacter(user, [skills: [skill1, skill2] as Set])
        when:
            def response = httpGet(characterSkillsUrl(character.id), CharacterSkillBasics, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.content.size() == 2
            SkillHelper.compare([skill1, skill2], response.body)
    }

    def "should learn skill"() {
        given:
            def skill1 = skillServiceHelper.createSkill(name: "Fireball")
            skillServiceHelper.createSkill(name: "Skill")
            def character = characterServiceHelper.createCharacter(user)
        when:
            def response = httpGet(skillLearnUrl(skill1.id, character.id), SkillLite, [accessToken: userAccessToken])
            character = characterServiceHelper.getCharacter(character.id)
        then:
            response.status == HttpStatus.OK
            SkillHelper.compare(skill1, response.body)
            character.skills.first().level == 1
            SkillHelper.compare(character.skills.first().skill, skill1)
    }
}
