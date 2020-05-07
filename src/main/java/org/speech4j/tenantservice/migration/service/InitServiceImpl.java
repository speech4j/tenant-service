package org.speech4j.tenantservice.migration.service;

import liquibase.exception.LiquibaseException;
import org.speech4j.tenantservice.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import static org.speech4j.tenantservice.config.multitenancy.MultiTenantConstants.SQL_CREATE_SCHEMA;

@Service
public class InitServiceImpl implements InitService {
    @Value("${liquibase.general_changelog}")
    private String generalChangelogFile;

    private LiquibaseService liquibaseService;
    private DataSource dataSource;

    @Autowired
    public InitServiceImpl(LiquibaseService liquibaseService,
                           DataSource dataSource) {
        this.liquibaseService = liquibaseService;
        this.dataSource = dataSource;
    }

    @Override
    public void initSchema(List<String> tenants) {
        if (tenants != null && !tenants.isEmpty()){
            tenants.forEach(tenant->{

                try(Connection connection = dataSource.getConnection()) {

                    try (Statement st = connection.createStatement()) {
                        st.executeUpdate(String.format(SQL_CREATE_SCHEMA, tenant));
                        connection.setSchema(tenant);

                        liquibaseService.updateSchema(connection, generalChangelogFile, tenant);
                    }

               } catch (SQLException | LiquibaseException  e) {
                    throw new InternalServerException("Error during updating of database!");
                }

            });

        }
    }
}
