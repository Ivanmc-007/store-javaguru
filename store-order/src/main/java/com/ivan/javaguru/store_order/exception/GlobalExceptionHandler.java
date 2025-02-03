package com.ivan.javaguru.store_order.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static String EUROPE_MINSK = "Europe/Minsk";

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerProductNotFoundException(ProductNotFoundException exception) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

    @ExceptionHandler(StoreProductCatalogIsNotAvailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleStoreProductCatalogIsNotAvailableException(
            StoreProductCatalogIsNotAvailableException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Connection refused",
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK))
        );
    }

}
