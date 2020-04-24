package com.speech4j.tenantservice.liquibase.service;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class LiquibaseService {

    @Autowired
    private SpringLiquibase springLiquibase;

    @Autowired
    private DataSource dataSource;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LiquibaseService.class);

    public void run(String schemaName) {

        ClassLoaderResourceAccessor resourceAcessor = new ClassLoaderResourceAccessor(
                getClass().getClassLoader());

        try {

            Connection c = dataSource.getConnection();

            c.createStatement()
                    .executeUpdate("CREATE SCHEMA IF NOT EXISTS " + schemaName);

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(c));

            database.setLiquibaseSchemaName(schemaName);
            database.setDefaultSchemaName(schemaName);
            new Liquibase("db/changelog/db.changelog-default.yaml",
                    resourceAcessor, database)
                    .update(springLiquibase.getContexts());
        } catch (SQLException | LiquibaseException e) {
            LOGGER.error("Liquibase service error", e);
        }

    }
}
