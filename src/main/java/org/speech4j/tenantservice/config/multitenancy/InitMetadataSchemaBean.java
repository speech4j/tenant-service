package org.speech4j.tenantservice.config.multitenancy;


import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.DEFAULT_METADATA;
import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.SQL_CREATE_SCHEMA;

@Component
@Slf4j
public class InitMetadataSchemaBean {
    @Autowired
    private PostgresqlConnectionConfiguration.Builder builder;

    @PostConstruct
    public void init() {
        PostgresqlConnectionFactory connectionFactory = new PostgresqlConnectionFactory(
                builder.schema(DEFAULT_METADATA).build()
        );
        DatabaseClient client = DatabaseClient.create(connectionFactory);
        client.execute(String.format(SQL_CREATE_SCHEMA, DEFAULT_METADATA)).then().block();
        log.debug("INIT-METADATA-SCHEMA: Schema metadata was created successfully!");

        List<TenantDtoResp> tenants =
                client.execute("SELECT * FROM tenants")
                        .as(TenantDtoResp.class).fetch().all().collectList().block();
        TenantConnectionFactoryHolder.setMap(tenants, builder);
    }


}
