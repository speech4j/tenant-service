package org.speech4j.tenantservice.config.datasource;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.speech4j.tenantservice.config.multitenancy.RoutingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.connectionfactory.lookup.MapConnectionFactoryLookup;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.Collections;

import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.DEFAULT_METADATA;
import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.DEFAULT_TENANT_ID;

@Configuration("postgresConfig")
@EnableR2dbcRepositories
public class PostgresConfig {
    @Bean
    public PostgresqlConnectionConfiguration.Builder postgresqlConnectionConfiguration(
            @Value("${db.host}") String host,
            @Value("${db.port}") int port,
            @Value("${db.name}") String database,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password
    ) {
        return PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .database(database)
                .username(username)
                .password(password);
    }


    @Bean
    public RoutingConnectionFactory getRoute(PostgresqlConnectionConfiguration.Builder builder) {
        RoutingConnectionFactory connectionFactory = new RoutingConnectionFactory();
        connectionFactory.setDefaultTargetConnectionFactory(
                new PostgresqlConnectionFactory(builder.schema(DEFAULT_METADATA).build())
        );
        connectionFactory.setTargetConnectionFactories(
                Collections.singletonMap(DEFAULT_TENANT_ID, new PostgresqlConnectionFactory(builder.schema(DEFAULT_TENANT_ID).build()))
        );
        connectionFactory.setConnectionFactoryLookup(new MapConnectionFactoryLookup());
        connectionFactory.afterPropertiesSet();

        return connectionFactory;
    }
}

