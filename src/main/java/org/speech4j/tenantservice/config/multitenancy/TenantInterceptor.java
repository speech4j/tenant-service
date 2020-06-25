package org.speech4j.tenantservice.config.multitenancy;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.exception.TenantNotFoundException;
import org.speech4j.tenantservice.repository.metadata.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@Component
public class TenantInterceptor implements WebFilter {

    private TenantRepository tenantRepository;

    @Autowired
    public TenantInterceptor(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String schema = exchange.getRequest().getURI().getPath();
        if (schema != null && !schema.isBlank() && schema.length() >= "/tenants/".length()) {
            schema = schema.substring("/tenants/".length());
            if (schema.contains("/")) {
                schema = schema.substring(0, schema.indexOf("/"));

                String finalSchema = schema;
                getTenantIds().doOnSuccess(list -> {
                    if (!list.contains(finalSchema)){
                        Mono.error(new TenantNotFoundException("Tenant by a specified id: [" + finalSchema + "] not found!"));
                    }
                }).subscribe();

            }
        }

        return chain.filter(exchange);
    }

    private Mono<List<String>> getTenantIds() {
        return tenantRepository.findAll()
                .map(t -> t.getId())
                .collectList();
    }
}
