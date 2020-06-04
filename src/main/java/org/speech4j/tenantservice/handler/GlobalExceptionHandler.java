package org.speech4j.tenantservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.dto.handler.ResponseMessageDto;
import org.speech4j.tenantservice.exception.DuplicateEntityException;
import org.speech4j.tenantservice.exception.EntityNotFoundException;
import org.speech4j.tenantservice.exception.InternalServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ResponseMessageDto> handleEntityNotFoundException(Exception e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessageDto(e.getMessage()));
    }

    @ExceptionHandler({InternalServerException.class})
    public ResponseEntity<ResponseMessageDto> handleInternalServerException(Exception e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseMessageDto(e.getMessage()));
    }

    @ExceptionHandler({DuplicateEntityException.class})
    public ResponseEntity<ResponseMessageDto> handleDuplicateEntityException(Exception e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseMessageDto(e.getMessage()));
    }
}
