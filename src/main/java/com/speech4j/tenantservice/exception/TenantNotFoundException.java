package com.speech4j.tenantservice.exception;

public class TenantNotFoundException extends EntityNotFoundException {
    public TenantNotFoundException() {
        super();
    }

    public TenantNotFoundException(String message) {
        super(message);
    }

    public TenantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TenantNotFoundException(Throwable cause) {
        super(cause);
    }
}
