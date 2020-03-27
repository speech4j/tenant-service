package com.speech4j.tenantservice.controller;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.Assert.assertTrue;

@TestPropertySource("classpath:application-test.yaml")
@ActiveProfiles("test")
public class AbstractContainerBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractContainerBaseTest.class);
    static final PostgreSQLContainer postgreSQLContainer;

    @Value("${TEST_POSTGRES_PASSWORD}")
    private String password;
    @Value("${TEST_POSTGRES_USERNAME}")
    private String username;
    @Value("${TEST_POSTGRES_DB}")
    private String database;

    static {
        postgreSQLContainer = new PostgreSQLContainer()
                .withPassword("postgres")
                .withUsername("postgres")
                .withDatabaseName("tenant_db");
        postgreSQLContainer.start();

        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
    }

    @Test
    void isRunningContainer(){
        assertTrue(postgreSQLContainer.isRunning());
        LOGGER.info("CONTAINER IS RUNNING!" + database);
    }
}
