package com.ramosvji.WhatsApp_webhook.infrastructure.inbound.rest;

import com.ramosvji.WhatsApp_webhook.application.dto.WhatsAppRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * HandlerRequestController
 * </p>
 *
 * @author José Ignacio Ramos Vanegas
 * @version 1.0
 * @since 2024-08-01
 */
@RestController
@RequestMapping(path="/ramosvji/api/webhook")
public class HandlerRequestController {

    private final Logger logger = LoggerFactory.getLogger(HandlerRequestController.class);

    @PostMapping(path="/v01/webhook")
    public ResponseEntity<Object> handleRequestController(
            final @Valid @RequestBody WhatsAppRequest body) {

        //TODO Implement code

        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.OK);
    }
}
