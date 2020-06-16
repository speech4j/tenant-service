package org.speech4j.tenantservice.exception;

public class SqlOperationException extends RuntimeException {
    public SqlOperationException(String message) {
        super(message);
    }
}
