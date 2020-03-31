package com.speech4j.tenantservice.exception;

public class TenantNotFoundException extends EntityNotFoundException {
    public TenantNotFoundException(String message) {
        super(message);
    }
}
