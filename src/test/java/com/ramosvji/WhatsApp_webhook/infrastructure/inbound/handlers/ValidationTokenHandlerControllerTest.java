package com.ramosvji.WhatsApp_webhook.infrastructure.inbound.handlers;

import com.ramosvji.WhatsApp_webhook.application.dto.ExceptionResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ValidationTokenHandlerControllerTest {

    @InjectMocks
    private ValidationTokenHandlerController handlerController;

    @Mock
    private ConstraintViolation<?> mockViolation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("MissingServletRequestParameterException validation")
    public void testHandleMissingServletRequestParameterException() {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("param", "String");
        ResponseEntity<ExceptionResponse> response = handlerController.handler(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().messages().size());
        assertEquals("Required request parameter 'param' for method parameter type String is not present",
                response.getBody().messages().get(0));
    }

    @Test
    @DisplayName("ConstraintViolationException validation")
    public void testHandleConstraintViolationException() {
        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("field");
        when(mockViolation.getPropertyPath()).thenReturn(mockPath);
        when(mockViolation.getMessage()).thenReturn("must not be null");

        Set<ConstraintViolation<?>> violations = Collections.singleton(mockViolation);
        ConstraintViolationException exception = new ConstraintViolationException(violations);
        ResponseEntity<ExceptionResponse> response = handlerController.handler(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().messages().size());
        assertEquals("field: must not be null", response.getBody().messages().get(0));
    }

}