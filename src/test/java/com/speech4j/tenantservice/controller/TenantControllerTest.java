package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.AbstractContainerBaseTest;
import com.speech4j.tenantservice.TenantServiceApplication;
import com.speech4j.tenantservice.dto.handler.ResponseMessageDto;
import com.speech4j.tenantservice.dto.request.TenantDtoReq;
import com.speech4j.tenantservice.dto.response.TenantDtoResp;
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

import static com.speech4j.tenantservice.util.DataUtil.getListOfTenants;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TenantControllerTest extends AbstractContainerBaseTest {
   @Autowired
    private TestRestTemplate template;

    private HttpHeaders headers;
    private HttpEntity<TenantDtoReq> request;
    private TenantDtoReq testTenant;

    private final String exceptionMessage = "Tenant not found!";
    private String testId;
    private List<TenantDtoReq> tenantsList;

    @BeforeEach
    void setUp() throws URISyntaxException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Initializing of test tenant
        testTenant = new TenantDtoReq();
        testTenant.setName("SoftServe");

        request = new HttpEntity<>(testTenant, headers);

        //Populating of db
        tenantsList = getListOfTenants();
        testId = populateDB(template, headers, tenantsList);
    }

    @Test
    public void findByIdTest_successFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<TenantDtoResp> response
                = template.exchange("/tenants/" + testId, HttpMethod.GET, request, TenantDtoResp.class);

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
    public void createEntityTest_successFlow() {
        final String url = "/tenants";

        ResponseEntity<TenantDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, TenantDtoResp.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void createEntityTest_unsuccessFlow() {
        final String url = "/tenants";

        //Make entity null
        request = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createEntityTestWithMissedRequiredField_unsuccessFlow() {
        final String url = "/tenants";

        testTenant.setName(null);
        request = new HttpEntity<>(testTenant, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation missed field
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Validation failed for object='tenantDtoReq'. Error count: 1", response.getBody().getMessage());
    }

    @Test
    public void updateEntityTest_successFlow() {
        final String url = "/tenants/" + testId;

        testTenant.setName("New Company");
        request = new HttpEntity<>(testTenant, headers);

        ResponseEntity<TenantDtoResp> response =
                this.template.exchange(url, HttpMethod.PUT, request, TenantDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(testTenant).isEqualToIgnoringGivenFields(response.getBody(), "createdDate");
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void updateEntityTest_unsuccessFlow() {
        final String url = "/tenants/" + 0;

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

    public String populateDB(TestRestTemplate template, HttpHeaders headers, List<TenantDtoReq> list) throws URISyntaxException {
        final String url = "/tenants";
        URI uri = new URI(url);

        ResponseEntity<TenantDtoResp> response1 = template.postForEntity(uri, new HttpEntity<>(list.get(0), headers), TenantDtoResp.class);
        ResponseEntity<TenantDtoResp> response2 = template.postForEntity(uri, new HttpEntity<>(list.get(1), headers), TenantDtoResp.class);
        return response1.getBody().getId();
    }
}
