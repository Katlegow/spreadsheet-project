package com.kmsolutions.exceptions;

public class MaximumFileColumnsExceeded extends RuntimeException {
    public MaximumFileColumnsExceeded(String message) {
        super(message);
    }

    public MaximumFileColumnsExceeded(String message, Throwable throwable) {
        super(message, throwable);
    }
}
