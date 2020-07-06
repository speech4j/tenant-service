package org.speech4j.tenantservice.config.multitenancy;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TenantConnectionFactoryHolder {
    private static ConcurrentHashMap<String, PostgresqlConnectionFactory> factories = new ConcurrentHashMap();

    public static PostgresqlConnectionFactory getFactory(String tenantId) {
        return factories.get(tenantId);
    }

    public static ConcurrentHashMap<String, PostgresqlConnectionFactory> getFactories() {
        return factories;
    }

    public static void addFactory(String tenantId, PostgresqlConnectionFactory factory) {
        factories.put(tenantId, factory);
        log.debug("TenantConnectionFactoryHolder: Connection factory for tenant: [{}] was created!", tenantId);
    }

    public static void setMap(List<TenantDtoResp> tenants, PostgresqlConnectionConfiguration.Builder builder){
        tenants.stream().forEach(tenant-> {
            factories.put(
                    tenant.getId(),
                    new PostgresqlConnectionFactory(builder.schema(tenant.getId()).build())
            );
            log.debug("TenantConnectionFactoryHolder: Connection factory for tenant: [{}] was created!", tenant.getId());
        });
        log.debug("TenantConnectionFactoryHolder: An initial connection factories map was filled!");
    }
}
