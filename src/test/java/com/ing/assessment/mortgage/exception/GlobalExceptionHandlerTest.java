package com.ing.assessment.mortgage.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Handles ConstraintViolationException")
    void handlesConstraintViolation() throws Exception {
        mockMvc.perform(get("/test/exception/constraint"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception", is("ConstraintViolationException")));
    }

    @Test
    @DisplayName("Handles MissingServletRequestParameterException")
    void handlesMissingParam() throws Exception {
        mockMvc.perform(get("/test/exception/missing-param"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Missing required parameter")))
                .andExpect(jsonPath("$.exception", is("MissingServletRequestParameterException")));
    }

    @Test
    @DisplayName("Handles NoResourceFoundException")
    void handlesNotFound() throws Exception {
        mockMvc.perform(get("/test/exception/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("Not Found")));
    }

    @Test
    @DisplayName("Handles HttpRequestMethodNotSupportedException")
    void handlesMethodNotAllowed() throws Exception {
        mockMvc.perform(get("/test/exception/method-not-allowed"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.error", containsString("Method Not Allowed")));
    }

    @Test
    @DisplayName("Handles HttpMediaTypeNotSupportedException")
    void handlesMediaType() throws Exception {
        mockMvc.perform(get("/test/exception/media-type"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.error", containsString("Unsupported Media Type")));
    }

    @Test
    @DisplayName("Handles IllegalArgumentException")
    void handlesIllegalArgument() throws Exception {
        mockMvc.perform(get("/test/exception/illegal-arg"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception", is("IllegalArgumentException")))
                .andExpect(jsonPath("$.message", containsString("business rule violated")));
    }

    @Test
    @DisplayName("Handles generic uncaught Exception")
    void handlesUncaught() throws Exception {
        mockMvc.perform(get("/test/exception/other"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error", containsString("Internal Server Error")))
                .andExpect(jsonPath("$.exception", is("RuntimeException")));
    }
}
