package org.speech4j.tenantservice.config.multitenancy;


import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
    }
}
