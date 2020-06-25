package org.speech4j.tenantservice.config.multitenancy;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.TENANT_KEY;


@Slf4j
public class TenantRoutingConnectionFactory extends AbstractTenantConnectionFactory {
    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono.subscriberContext().filter(it -> it.hasKey(TENANT_KEY)).map(it -> it.get(TENANT_KEY));
    }
}
