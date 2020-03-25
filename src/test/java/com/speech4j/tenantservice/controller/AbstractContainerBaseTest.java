package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.TenantServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractContainerBaseTest extends PostgreSQLContainer<AbstractContainerBaseTest> {
    static final PostgreSQLContainer postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer()
                .withPassword("password")
                .withUsername("postgres")
                .withDatabaseName("tenant_db");
    }

    @Override
    public void start(){
        super.start();
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
    }
}
