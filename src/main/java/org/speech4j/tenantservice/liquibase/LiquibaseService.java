package org.speech4j.tenantservice.liquibase;

import liquibase.exception.LiquibaseException;

import java.sql.Connection;

public interface LiquibaseService {
    void updateSchema(Connection connection, String changelogFile, String persistentTenant) throws LiquibaseException;
}
