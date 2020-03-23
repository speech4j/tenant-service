package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.ConfigDto;
import com.speech4j.tenantservice.dto.TenantDto;
import com.speech4j.tenantservice.entity.Tenant;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class TenantControllerTest extends AbstractContainerBaseTest {
    @LocalServerPort
    private int port;
    private TestRestTemplate template;
    private String baseUrl;

    private HttpHeaders headers;
    private HttpEntity<TenantDto> request;
    private TenantDto testTenant;


    @BeforeEach
    void setUp() {
        template = new TestRestTemplate();
        baseUrl = "http://localhost:" + port;

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Initializing of test tenant
        testTenant = new TenantDto();
        testTenant.setName("SoftServe");
        testTenant.setActive(true);

        request = new HttpEntity<>(testTenant, headers);
    }

    @Test
    void isRunningContainer() throws URISyntaxException {
        assertTrue(postgreSQLContainer.isRunning());

        //Populating of db
        populateDB();
    }

    private void populateDB() throws URISyntaxException {
        final String url = baseUrl + "/tenants";
        URI uri = new URI(url);

        //entity1
        TenantDto tenant1 = new TenantDto();
        tenant1.setName("Company1");
        tenant1.setActive(true);

        //entity2
        TenantDto tenant2 = new TenantDto();
        tenant2.setName("Company2");
        tenant2.setActive(true);

        template.postForEntity(uri, new HttpEntity<>(tenant1, headers), TenantDto.class);
        template.postForEntity(uri, new HttpEntity<>(tenant2, headers), TenantDto.class);
    }
}
