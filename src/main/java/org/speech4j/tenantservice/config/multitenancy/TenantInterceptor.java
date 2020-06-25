package org.speech4j.tenantservice.config.multitenancy;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class TenantInterceptor implements WebFilter {

    private TenantService service;

    @Autowired
    public TenantInterceptor(TenantService service) {
        this.service = service;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String url = exchange.getRequest().getURI().getPath();
        if (url != null && !url.isBlank() && url.length() >= "/tenants/".length()) {
            url = url.substring("/tenants/".length());
            if (url.contains("/")) {
                String schema = url.substring(0, url.indexOf("/"));
                log.debug("INTERCEPTOR: Current schema was found: [{}]!", schema);

                return service.getById(schema).flatMap(response-> chain.filter(exchange));
            }
        }
        return chain.filter(exchange);


    }
}