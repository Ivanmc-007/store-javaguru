package com.ivan.javaguru.store_email_notification.exception;

public class NonRetryableException extends RuntimeException {
    public NonRetryableException() {
    }

    public NonRetryableException(String message, Throwable cause) {
        super(message, cause);
    }
}
