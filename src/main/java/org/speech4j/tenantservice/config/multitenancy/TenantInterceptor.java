package org.speech4j.tenantservice.config.multitenancy;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
public class TenantInterceptor implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public Mono<ServerResponse> filter(ServerRequest serverRequest,
                                       HandlerFunction<ServerResponse> handlerFunction) {
        String id = serverRequest.pathVariable("tenantId");
        if (id != null) {
            TenantContext.setCurrentTenant(id);
        }
        return handlerFunction.handle(serverRequest);
    }
}
