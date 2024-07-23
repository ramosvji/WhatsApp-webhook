package com.ramosvji.WhatsApp_webhook.infrastructure.inbound.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Verify webhook token
 * </p>
 *
 * @author José Ignacio Ramos Vanegas
 * @version 1.0
 * @since 2024-07-21
 */
@RestController
@RequestMapping(path="/ramosvji/api/webhook")
@Validated
public class VerificationTokenController {

    private final Logger logger = LoggerFactory.getLogger(VerificationTokenController.class);

    @Value("${verification.token}")
    private String TOKEN;

    /**
     * Verifies the token and returns the challenge if it is correct.
     *
     * @param verifyToken The verification token received in the request.
     * @param challenge   The challenge received in the request.
     * @return The challenge if the token is correct, otherwise null.
     */
    @GetMapping(path="/v01/webhook")
    public ResponseEntity<String> verifyToken(@Valid @NotBlank(message = "hub.verify_token must not be empty")
                               @RequestParam(name = "hub.verify_token") String verifyToken,
                               @Valid @NotBlank(message = "hub.challenge must not be empty")
                               @RequestParam(name = "hub.challenge") String challenge) {

        logger.info("Petición recibida");

        if(!verifyToken.equals(TOKEN)) {
            return new ResponseEntity<>(null,new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(challenge, new HttpHeaders(), HttpStatus.OK);
    }
}
