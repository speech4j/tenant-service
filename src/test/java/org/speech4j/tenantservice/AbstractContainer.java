package org.speech4j.tenantservice;

import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("test")
public class AbstractContainer {
    static PostgreSQLContainer postgreSQLContainer;

    static {
        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:12.2")
                .withPassword("postgres")
                .withUsername("postgres")
                .withDatabaseName("tenant_db")
                .withInitScript("data/init_data.sql");
        postgreSQLContainer.start();

      //  System.setProperty("db.port", (String) postgreSQLContainer.getPortBindings().get(0));
      //  System.out.println(postgreSQLContainer.getPortBindings());
      //  System.out.println(postgreSQLContainer.getJdbcUrl());
    }

}