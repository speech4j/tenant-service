package com.speech4j.tenantservice.exception;

public class ConfigNotFoundException extends EntityNotFoundException {
    public ConfigNotFoundException(String message) {
        super(message);
    }
}
