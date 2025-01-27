package com.ivan.javaguru.store_order.exception;

import java.time.ZonedDateTime;

public record ErrorResponse(
        int statusCode,
        String message,
        ZonedDateTime dateTime
) {
}
