package org.speech4j.tenantservice.config.multitenancy;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.DEFAULT_TENANT_ID;

@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SpringLiquibase springLiquibase;

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
            if (tenantIdentifier != null && !tenantIdentifier.equals(DEFAULT_TENANT_ID)) {
                // Create the schema
                String persistentTenant = tenantIdentifier.equals("speech4j") ? tenantIdentifier : "tenant_" + tenantIdentifier;

                connection.createStatement().executeUpdate("CREATE SCHEMA IF NOT EXISTS " + persistentTenant);
                connection.setSchema(persistentTenant);

                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(connection));

                ClassLoaderResourceAccessor resourceAcessor =
                        new ClassLoaderResourceAccessor(getClass().getClassLoader());

                database.setLiquibaseSchemaName(persistentTenant);
                database.setDefaultSchemaName(persistentTenant);
                new Liquibase(
                        "db/changelog/db.changelog-default.yaml",
                        resourceAcessor, database)
                        .update(springLiquibase.getContexts());


            } else {
                connection.setSchema(DEFAULT_TENANT_ID);
            }
        }
        catch (SQLException | LiquibaseException e ) {
            throw new HibernateException(
                    "Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e
            );
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        try {
            connection.setSchema(DEFAULT_TENANT_ID);
        }
        catch ( SQLException e ) {
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
