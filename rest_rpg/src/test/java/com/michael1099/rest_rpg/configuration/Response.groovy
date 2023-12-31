package com.michael1099.rest_rpg.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import lombok.Getter
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse

@Getter
class Response<T> {

    private ObjectMapper objectMapper
    private MockHttpServletResponse response
    HttpStatus status
    T body
    String errorMessage

    Response(MockHttpServletResponse response, ObjectMapper objectMapper, Class<T> requiredType) {
        this.response = response
        this.objectMapper = objectMapper
        this.status = HttpStatus.valueOf(response.status)
        if (response.contentType != null) {
            this.body = objectMapper.readValue(response.contentAsString, requiredType)
        }
        this.errorMessage = response.errorMessage
    }
}
