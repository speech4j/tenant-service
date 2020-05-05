package org.speech4j.tenantservice.config.multitenancy;

import liquibase.exception.LiquibaseException;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.speech4j.tenantservice.liquibase.LiquibaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.DEFAULT_TENANT_ID;
import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.SQL_CREATE_SCHEMA;

@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

    @Value("${liquibase.general_changelog}")
    private String generalChangelogFile;

    private transient DataSource dataSource;
    private transient LiquibaseService liquibaseService;

    @Autowired
    public MultiTenantConnectionProviderImpl(DataSource dataSource,
                                             LiquibaseService liquibaseService) {
        this.dataSource = dataSource;
        this.liquibaseService = liquibaseService;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        try {
            if (tenantIdentifier != null) {
                // Create the schema
                String persistentTenant = tenantIdentifier.equals("speech4j") ? tenantIdentifier : "tenant_" + tenantIdentifier;

                try (Statement ps = connection.createStatement()) {

                    ps.executeUpdate(String.format(SQL_CREATE_SCHEMA, persistentTenant));
                    connection.setSchema(persistentTenant);

                    //Updating of schema
                    liquibaseService.updateSchema(connection, generalChangelogFile, persistentTenant);
                }

            } else {
                connection.setSchema(DEFAULT_TENANT_ID);
            }
        } catch (SQLException | LiquibaseException e) {
            throw new HibernateException(
                    "Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
        }

        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        try {
            connection.setSchema(DEFAULT_TENANT_ID);
        } catch (SQLException e) {
            throw new HibernateException(
                    "Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e
            );
        }
        connection.close();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }
}
