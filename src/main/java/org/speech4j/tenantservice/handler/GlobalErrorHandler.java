package org.speech4j.tenantservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.exception.DuplicateEntityException;
import org.speech4j.tenantservice.exception.EntityNotFoundException;
import org.speech4j.tenantservice.exception.ValidationEntityException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        Map<String, String> errorBody = new HashMap<>();
        if (ex instanceof DuplicateEntityException || ex instanceof ValidationEntityException) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            errorBody.put("message", ex.getMessage());
            log.error("EXCEPTION-HANDLER: 400 error was discovered! " +
                    "MESSAGE: <== {} ==>", ex.getLocalizedMessage());
            return getJsonMessage(exchange, bufferFactory, errorBody);
        }
        if (ex instanceof EntityNotFoundException) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            errorBody.put("message", ex.getMessage());
            log.error("ERROR-HANDLER: 404 error was discovered!  " +
                    "MESSAGE: <== {} ==>", ex.getLocalizedMessage());
            return getJsonMessage(exchange, bufferFactory, errorBody);
        }

        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        errorBody.put("message", ex.getLocalizedMessage());
        log.error("EXCEPTION-HANDLER: 500 error was discovered! " +
                "MESSAGE: <== {} ==>", ex.getLocalizedMessage());
        DataBuffer dataBuffer = bufferFactory.wrap(mapper.writeValueAsBytes(errorBody));
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    private Mono<Void> getJsonMessage(ServerWebExchange exchange,
                                      DataBufferFactory bufferFactory,
                                      Map<String, String> errorBody) {
        DataBuffer dataBuffer;
        try {
            dataBuffer = bufferFactory.wrap(mapper.writeValueAsBytes(errorBody));
        } catch (JsonProcessingException e) {
            dataBuffer = bufferFactory.wrap("".getBytes());
        }
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
