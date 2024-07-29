package com.ramosvji.WhatsApp_webhook.infrastructure.inbound.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ramosvji.WhatsApp_webhook.application.dto.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = {"verification.token=ASF"})
public class VerificationTokenControllerTest {

    @InjectMocks
    private VerificationTokenController verificationTokenController;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final String URL = "/ramosvji/api/webhook/v01/webhook";
    private static final String VALID_TOKEN = "ASF";
    private static final String VERIFY_TOKEN = "hub.verify_token";
    private static final String CHALLENGE = "hub.challenge";
    private static final String CHALLENGE_VALUE = "challenge";

    private static final String TOKEN_MESSAGE_ERROR = """
    Required request parameter 'hub.verify_token' for method parameter type String is not present
    """;
    private static final String CHALLENGE_MESSAGE_ERROR = """
    Required request parameter 'hub.challenge' for method parameter type String is not present
    """;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Single test successful")
    public void testVerifyTokenSuccess() throws Exception {
        mockMvc.perform(get(URL)
                        .param(VERIFY_TOKEN, VALID_TOKEN)
                        .param(CHALLENGE, CHALLENGE_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(CHALLENGE_VALUE));
    }

    @Test
    @DisplayName("Invalid token test")
    public void testVerifyTokenFailure() throws Exception {
        final String invalidToken = "ASDFGHJKY42M";

        mockMvc.perform(get(URL)
                        .param(VERIFY_TOKEN, invalidToken)
                        .param(CHALLENGE, CHALLENGE_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("The hub.verify_token param is missing")
    public void testVerifyTokenMissingVerifyToken() throws Exception {
        testQueryParams(
                get(URL).param(CHALLENGE, CHALLENGE_VALUE),
                TOKEN_MESSAGE_ERROR
        );
    }

    @Test
    @DisplayName("The hub.challenge param is missing")
    public void testVerifyTokenMissingChallenge() throws Exception {
        testQueryParams(
                get(URL).param(VERIFY_TOKEN, VALID_TOKEN),
                CHALLENGE_MESSAGE_ERROR
        );
    }

    private void testQueryParams(MockHttpServletRequestBuilder requestBuilder,
                                 String expectedErrorMessage) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MissingServletRequestParameterException))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
                    ExceptionResponse response = objectMapper.readValue(json, ExceptionResponse.class);

                    assertEquals(expectedErrorMessage.trim(), response.messages().get(0));
                    assertNotNull(response.dateTime());
                });
    }

}