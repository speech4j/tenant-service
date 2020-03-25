package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.handler.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

public class AbstractContainerBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

        LOGGER.info("URL:" + System.getProperty("spring.datasource.url"));
    }
}
