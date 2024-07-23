package com.ramosvji.WhatsApp_webhook.application.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * A record representing the structure of the global exception response.
 * </p>
 *
 * @author José Ignacio Ramos Vanegas
 * @version 1.0
 * @since 2024-07-22
 *
 * @param messages  the list of messages
 * @param dateTime the timestamp of the exception
 */
public record ExceptionResponse(List<String> messages, LocalDateTime dateTime) {
}
