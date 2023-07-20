package com.michael1099.rest_rpg.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TestBase extends Specification {

    @SpringBean
    JavaMailSender mailSender = Stub()

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    <T> Response<T> httpPost(String url, Object request, Class<T> requiredType, MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>()) {
        def response = mvc.perform(post(url)
                .content(asJsonString(request))
                .params(parameters)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().response
        return new Response<T>(response, objectMapper, requiredType)
    }

    <T> Response<T> httpGet(String url, Class<T> requiredType, MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>()) {
        def response = mvc.perform(get(url)
                .params(parameters)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().response
        return new Response<T>(response, objectMapper, requiredType)
    }

    private static asJsonString(final Object obj) {
        try {
            def mapper = new ObjectMapper()
            def jsonContent = mapper.writeValueAsString(obj)
            return jsonContent
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }
}
