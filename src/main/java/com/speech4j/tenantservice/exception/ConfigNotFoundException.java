package com.speech4j.tenantservice.exception;

public class ConfigNotFoundException extends EntityNotFoundException {
    public ConfigNotFoundException() {
        super();
    }

    public ConfigNotFoundException(String message) {
        super(message);
    }

    public ConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigNotFoundException(Throwable cause) {
        super(cause);
    }
}
