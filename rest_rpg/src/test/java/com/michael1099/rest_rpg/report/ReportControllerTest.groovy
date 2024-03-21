package com.michael1099.rest_rpg.report

import com.michael1099.rest_rpg.character.CharacterServiceHelper
import com.michael1099.rest_rpg.configuration.TestBase
import com.michael1099.rest_rpg.helpers.DeleteServiceHelper
import org.openapitools.model.GenerateReportRequest
import org.openapitools.model.ReportResponse
import org.openapitools.model.ReportType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class ReportControllerTest extends TestBase {

    def generateUrl = "/report/generate"

    @Autowired
    DeleteServiceHelper deleteServiceHelper

    @Autowired
    CharacterServiceHelper characterServiceHelper

    void cleanup() {
        deleteServiceHelper.clean()
    }

    def "should create report"() {
        given:
            def character = characterServiceHelper.createCharacter(user, [name: "Jan"])
            def request = new GenerateReportRequest(character.id, ReportType.CHARACTER)
        when:
            def response = httpPost(generateUrl, request, ReportResponse, [accessToken: userAccessToken])
        then:
            response.status == HttpStatus.OK
            response.body.name == "Character " + character.getName() + " report"
    }
}
