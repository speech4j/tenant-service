package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.TenantServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AbstractContainerBaseTest {
    static final PostgreSQLContainer postgreSQLContainer;


    static {
        postgreSQLContainer = new PostgreSQLContainer()
                .withPassword("password")
                .withUsername("postgres")
                .withDatabaseName("tenant_db");
        postgreSQLContainer.start();

        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
    }
}
