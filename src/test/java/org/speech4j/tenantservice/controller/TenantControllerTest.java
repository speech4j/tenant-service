package org.speech4j.tenantservice.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.speech4j.tenantservice.AbstractContainerBaseTest;
import org.speech4j.tenantservice.TenantServiceApplication;
import org.speech4j.tenantservice.dto.request.TenantDtoCreateReq;
import org.speech4j.tenantservice.dto.response.ResponseMessageDto;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;
import org.speech4j.tenantservice.fixture.DataFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TenantControllerTest extends AbstractContainerBaseTest {
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private CleanService cleanService;

    private HttpHeaders headers;
    private HttpEntity<TenantDtoCreateReq> request;
    private TenantDtoCreateReq testTenant;

    private final String exceptionMessage = "Tenant not found!";
    private String testId;
    private List<TenantDtoCreateReq> tenantsList;

    @BeforeEach
    void setUp() throws URISyntaxException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Initializing of test tenant
        testTenant = new TenantDtoCreateReq();
        testTenant.setName("soft_serve");
        testTenant.setDescription("Company");

        request = new HttpEntity<>(testTenant, headers);

        //Populating of db
        tenantsList = DataFixture.getListOfTenants();
        testId = populateDB(template, headers, tenantsList)[0];
    }

    @AfterEach
    void cleanUp() throws SQLException {
        cleanService.cleanUp("metadata.tenants");
    }

    @Test
    void findTenantByIdTest_successFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<TenantDtoResp> response
                = template.exchange("/tenants/" + testId, HttpMethod.GET, request, TenantDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void findTenantByIdTest__unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange("/tenants/0", HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    void createTenantTest_successFlow() {
        final String url = "/tenants";

        ResponseEntity<TenantDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, TenantDtoResp.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void createTenantTest_unsuccessFlow() {
        final String url = "/tenants";

        //Make entity null
        request = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void createTenantTestWithMissedRequiredField_unsuccessFlow() {
        final String url = "/tenants";

        testTenant.setName(null);
        request = new HttpEntity<>(testTenant, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation missed field
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Validation failed for object='tenantDtoCreateReq'. Error count: 1", response.getBody().getMessage());
    }

    @Test
    void updateTenantTest_successFlow() {
        final String url = "/tenants/" + testId;

        testTenant.setDescription("New Company");
        request = new HttpEntity<>(testTenant, headers);

        ResponseEntity<TenantDtoResp> response =
                this.template.exchange(url, HttpMethod.PUT, request, TenantDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(testTenant).isEqualToIgnoringGivenFields(response.getBody(), "createdDate", "modifiedDate", "name");
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateTenantTest_unsuccessFlow() {
        final String url = "/tenants/" + 0;

        testTenant.setName("New Company");
        request = new HttpEntity<>(testTenant, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    void deleteTenant_successFlow() {
        final String url = "/tenants/" + testId;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteTenant_unsuccessFlow() {
        final String url = "/tenants/" + 0;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    void findAllTenantsTest() {
        request = new HttpEntity<>(headers);
        ResponseEntity<List<TenantDtoResp>> response =
                template.exchange("/tenants", HttpMethod.GET, request, new ParameterizedTypeReference<List<TenantDtoResp>>() {
                });

        //Checking if status code is correct
        assertEquals(200, response.getStatusCodeValue());
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response) {
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    public String[] populateDB(TestRestTemplate template, HttpHeaders headers, List<TenantDtoCreateReq> list) throws URISyntaxException {
        final String url = "/tenants";
        URI uri = new URI(url);
        String[] idList = new String[2];

        ResponseEntity<TenantDtoResp> response1 = template.postForEntity(uri, new HttpEntity<>(list.get(0), headers), TenantDtoResp.class);
        ResponseEntity<TenantDtoResp> response2 = template.postForEntity(uri, new HttpEntity<>(list.get(1), headers), TenantDtoResp.class);
        idList[0] = response1.getBody().getId();
        idList[1] = response2.getBody().getId();

        return idList;
    }
}
