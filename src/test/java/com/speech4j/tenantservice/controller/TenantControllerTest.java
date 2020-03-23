package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.ConfigDto;
import com.speech4j.tenantservice.dto.TenantDto;
import com.speech4j.tenantservice.dto.handler.ResponseMessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TenantControllerTest extends AbstractContainerBaseTest {
    @LocalServerPort
    private int port;
    private TestRestTemplate template;
    private String baseUrl;

    private HttpHeaders headers;
    private HttpEntity<TenantDto> request;
    private TenantDto testTenant;

    private final String exceptionMessage = "Tenant not found!";


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

    @Test
    public void findByIdTest_successFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<TenantDto> response
                = template.exchange(baseUrl + "/tenants/1", HttpMethod.GET, request, TenantDto.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void findByIdTest__unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(baseUrl + "/tenants/100", HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void addEntityTest_successFlow() {
        final String url = baseUrl + "/tenants";

        ResponseEntity<TenantDto> response =
                this.template.exchange(url, HttpMethod.POST, request, TenantDto.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void addEntityTest_unsuccessFlow() {
        final String url = baseUrl + "/tenants/users/configs";

        //Make entity null
        request = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void updateEntityTest_successFlow() {
        final String url = baseUrl + "/tenants/me";

        testTenant.setId(1l);
        testTenant.setName("New Company");
        request = new HttpEntity<>(testTenant, headers);

        ResponseEntity<TenantDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, TenantDto.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(testTenant).isEqualToIgnoringGivenFields(response.getBody(), "createdDate");
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void updateEntityTest_unsuccessFlow() {
        final String url = baseUrl + "/tenants/me";

        testTenant.setId(100l);
        testTenant.setName("New Company");
        request = new HttpEntity<>(testTenant, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void deleteEntity_successFlow() {
        Long id = 2l;
        final String url = baseUrl + "/tenants/" + id;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());

    }

    @Test
    public void deleteEntity_unsuccessFlow() {
        Long id = 100l;
        final String url = baseUrl + "/tenants/" + id;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void findAllTest() {
        List<ConfigDto> response = this.template.getForObject(baseUrl + "/tenants", List.class);
        assertEquals(1, response.size());
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response){
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
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
