package com.ramosvji.WhatsApp_webhook.infrastructure.inbound.handlers;

import com.ramosvji.WhatsApp_webhook.application.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Global exception handler for validation token related exceptions.
 * </p>
 *
 * @author José Ignacio Ramos Vanegas
 * @version 1.0
 * @since 2024-07-21
 */
@ControllerAdvice
public class ValidationTokenHandlerController {

    private final Logger logger = LoggerFactory.getLogger(ValidationTokenHandlerController.class);

    /**
     * Handles MissingServletRequestParameterException.
     *
     * When any of the query parameters requested as mandatory are not provided in the token validation request,
     * an error with HTTP status code 400 will be returned, along with a message specifying which field was not
     * provided.
     *
     * @param e the exception to be handled
     * @return a ResponseEntity containing an ExceptionResponse and HTTP status BAD_REQUEST (400)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handler(final MissingServletRequestParameterException e) {
        logger.error(e.getMessage());

        List<String> messages = Collections.singletonList(e.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse(messages, LocalDateTime.now());

        return new ResponseEntity<>(exceptionResponse,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
