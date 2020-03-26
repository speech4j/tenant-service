package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.TenantServiceApplication;
import com.speech4j.tenantservice.dto.request.TenantDtoReq;
import com.speech4j.tenantservice.dto.handler.ResponseMessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TenantControllerTest extends AbstractContainerBaseTest {
   @Autowired
    private TestRestTemplate template;

    private HttpHeaders headers;
    private HttpEntity<TenantDtoReq> request;
    private TenantDtoReq testTenant;

    private final String exceptionMessage = "Tenant not found!";
    private Long testId;


    @BeforeEach
    void setUp() throws URISyntaxException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Initializing of test tenant
        testTenant = new TenantDtoReq();
        testTenant.setName("SoftServe");
        testTenant.setActive(true);

        request = new HttpEntity<>(testTenant, headers);

        //Populating of db
        populateDB();
    }

    @Test
    void isRunningContainer(){
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    public void findByIdTest_successFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<TenantDtoReq> response
                = template.exchange("/tenants/" + testId, HttpMethod.GET, request, TenantDtoReq.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void findByIdTest__unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange("/tenants/0", HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void addEntityTest_successFlow() {
        final String url = "/tenants";

        ResponseEntity<TenantDtoReq> response =
                this.template.exchange(url, HttpMethod.POST, request, TenantDtoReq.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void addEntityTest_unsuccessFlow() {
        final String url = "/tenants/users/configs";

        //Make entity null
        request = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void updateEntityTest_successFlow() {
        final String url = "/tenants/me";

        testTenant.setId(testId);
        testTenant.setName("New Company");
        request = new HttpEntity<>(testTenant, headers);

        ResponseEntity<TenantDtoReq> response =
                this.template.exchange(url, HttpMethod.PUT, request, TenantDtoReq.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(testTenant).isEqualToIgnoringGivenFields(response.getBody(), "createdDate");
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void updateEntityTest_unsuccessFlow() {
        final String url = "/tenants/me";

        testTenant.setId(0l);
        testTenant.setName("New Company");
        request = new HttpEntity<>(testTenant, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void deleteEntity_successFlow() {
        final String url = "/tenants/" + testId;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void deleteEntity_unsuccessFlow() {
        final String url = "/tenants/" + 0;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void findAllTest() {
        request = new HttpEntity<>(headers);
        ResponseEntity<List> response = template.exchange("/tenants", HttpMethod.GET, request, List.class);

        //Checking if status code is correct
        assertEquals(200, response.getStatusCodeValue());
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response){
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    private void populateDB() throws URISyntaxException {
        final String url = "/tenants";
        URI uri = new URI(url);

        //entity1
        TenantDtoReq tenant1 = new TenantDtoReq();
        tenant1.setName("Company1");
        tenant1.setActive(true);

        //entity2
        TenantDtoReq tenant2 = new TenantDtoReq();
        tenant2.setName("Company2");
        tenant2.setActive(true);

        ResponseEntity<TenantDtoReq> response1 = template.postForEntity(uri, new HttpEntity<>(tenant1, headers), TenantDtoReq.class);
        ResponseEntity<TenantDtoReq> response2 = template.postForEntity(uri, new HttpEntity<>(tenant2, headers), TenantDtoReq.class);
        testId = response1.getBody().getId();
    }
}