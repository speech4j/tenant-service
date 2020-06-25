package org.speech4j.tenantservice.config.multitenancy;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.connectionfactory.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Mono;

import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.DEFAULT_METADATA;
import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.DEFAULT_TENANT_ID;
import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.TENANT_KEY;


@Slf4j
public class RoutingConnectionFactory extends AbstractRoutingConnectionFactory {
    @Autowired
    private PostgresqlConnectionConfiguration.Builder builder;

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono.subscriberContext().filter(it -> it.hasKey(TENANT_KEY)).map(it -> it.get(TENANT_KEY));
    }

    /*
     * (non-Javadoc)
     * @see io.r2dbc.spi.AbstractRoutingConnectionFactory#determineTargetConnectionFactory()
     */
    @Override
    protected Mono<ConnectionFactory> determineTargetConnectionFactory() {
        Mono<Object> lookupKey = determineCurrentLookupKey().defaultIfEmpty(DEFAULT_METADATA);

        return lookupKey.flatMap((key) -> {
                if (key.equals(DEFAULT_METADATA) || key.equals(DEFAULT_TENANT_ID)) {
                    log.debug("ROUTING-CONNECTION-FACTORY: Factory for schema: [{}] was set!", key);
                    return super.determineTargetConnectionFactory();
                } else {
                    log.debug("ROUTING-CONNECTION-FACTORY: Factory for schema: [{}] started the creating!", key);
                    return  Mono.just(new PostgresqlConnectionFactory(builder.schema((String) key).build()));
                }
        });

    }
}
