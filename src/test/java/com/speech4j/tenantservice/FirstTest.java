package com.speech4j.tenantservice;

import com.speech4j.tenantservice.entity.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

class FirstTest extends AbstractContainerBaseTest {
    @LocalServerPort
    private int port;
    private TestRestTemplate restTemplate;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        restTemplate = new TestRestTemplate();
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void isRunningContainer() {
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    public void findByIdTest() {
        Config response = this.restTemplate.getForObject(baseUrl + "/configs/1", Config.class);
        assertNotNull(response);
    }

}
