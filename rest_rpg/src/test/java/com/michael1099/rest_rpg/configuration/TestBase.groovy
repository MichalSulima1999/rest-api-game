package com.michael1099.rest_rpg.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TestBase extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    <T> Response<T> httpPost(String url, Object request, Class<T> requiredType) {
        def response = mvc.perform(post(url)
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().response
        return new Response<T>(response, objectMapper, requiredType)
    }

    <T extends Object> T asObject(MockHttpServletResponse response, Class<T> responseType) {
        return objectMapper.readValue(response.contentAsString, responseType)
    }

    private static asJsonString(final Object obj) {
        try {
            def mapper = new ObjectMapper();
            def jsonContent = mapper.writeValueAsString(obj);
            return jsonContent
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
