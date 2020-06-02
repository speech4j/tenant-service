package org.speech4j.tenantservice.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.speech4j.tenantservice.AbstractContainerBaseTest;
import org.speech4j.tenantservice.TenantServiceApplication;
import org.speech4j.tenantservice.dto.response.ResponseMessageDto;
import org.speech4j.tenantservice.dto.request.ConfigDtoReq;
import org.speech4j.tenantservice.dto.request.TenantDtoCreateReq;
import org.speech4j.tenantservice.dto.response.ConfigDtoResp;
import org.speech4j.tenantservice.entity.tenant.ApiName;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigControllerTest extends AbstractContainerBaseTest {
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private CleanService cleanService;

    private HttpHeaders headers;
    private HttpEntity<ConfigDtoReq> request;
    private ConfigDtoReq testConfig;

    private final String exceptionMessage = "Config not found!";
    private String []testConfigIds = new String[2];
    private String []testTenantIds;
    private List<ConfigDtoReq> configsList;
    private List<TenantDtoCreateReq> tenantsList;

    @BeforeEach
    void setUp() throws URISyntaxException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Initializing of test config
        testConfig = new ConfigDtoReq();
        testConfig.setApiName(ApiName.AZURE);
        Map<String,Object> credentials = new HashMap<>();
        credentials.put("username", "mslob");
        credentials.put("password", "qwerty123");
        testConfig.setCredentials(credentials);

        request = new HttpEntity<>(testConfig, headers);

        //Populating of db
        tenantsList = DataFixture.getListOfTenants();
        testTenantIds = new TenantControllerTest().populateDB(template, headers, tenantsList);
        configsList = DataFixture.getListOfConfigs();
        populateDB(configsList);
    }

    @AfterEach
    void cleanUp() throws SQLException {
        cleanService.cleanUp("metadata.tenants");
        cleanService.cleanUp("name1.tenant_configs");
    }

    @Test
    void findByIdTest_successFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ConfigDtoResp> response
                = template.exchange("/tenants/" + testTenantIds[0] + "/configs/" + testConfigIds[0], HttpMethod.GET, request, ConfigDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(configsList.get(0)).isEqualToIgnoringGivenFields(response.getBody(), "id");
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void findByIdTest__unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange("/tenants/" + testTenantIds[0] + "/configs/0", HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    void findByIdTestDifferentTenantId_unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange("/tenants/" + testTenantIds[1] + "/configs/" + testConfigIds[0], HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    void createConfigTest_successFlow() {
        final String url = "/tenants/" + testTenantIds[0] + "/configs";

        ResponseEntity<ConfigDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, ConfigDtoResp.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void createConfigTest_unsuccessFlow() {
        final String url = "/tenants/" + testTenantIds[0] + "/configs";

        //Make entity null
        request = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void updateConfigTest_successFlow() {
        final String url = "/tenants/" + testTenantIds[0] + "/configs/" + testConfigIds[0];

        testConfig.setApiName(ApiName.HEROKU);
        request = new HttpEntity<>(testConfig, headers);

        ResponseEntity<ConfigDtoReq> response =
                this.template.exchange(url, HttpMethod.PUT, request, ConfigDtoReq.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testConfig, response.getBody());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateConfigTest_unsuccessFlow() {
        final String url = "/tenants/" + testTenantIds[0] + "/configs/" + 0;

        testConfig.setApiName(ApiName.HEROKU);
        request = new HttpEntity<>(testConfig, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    void deleteConfig_successFlow() {
        final String url = "/tenants/" + testTenantIds[0] + "/configs/" + testConfigIds[0];

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());

    }

    @Test
    void deleteConfig_unsuccessFlow() {
        final String url = "/tenants/" + testTenantIds[0] + "/configs/" + 0;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    void findAllConfigsTest_successFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<List<ConfigDtoResp>> response = template.exchange("/tenants/" + testTenantIds[0] + "/configs",
                HttpMethod.GET, request, new ParameterizedTypeReference<List<ConfigDtoResp>>(){});

        //Checking if status code is correct
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void findAllConfigsTestByTenantId_unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response = template.exchange("/tenants/" + 0 + "/configs", HttpMethod.GET, request, ResponseMessageDto.class);
        System.out.println(response);

        //Checking if status code is correct
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Tenant with specified identifier [0] not found!", response.getBody().getMessage());
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response){
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    private void populateDB(List<ConfigDtoReq> list) throws URISyntaxException {
        final String url = "/tenants/" + testTenantIds[0] + "/configs/";
        URI uri = new URI(url);

        ResponseEntity<ConfigDtoResp> response1 = template.postForEntity(uri, new HttpEntity<>(list.get(0), headers), ConfigDtoResp.class);
        ResponseEntity<ConfigDtoResp> response2 = template.postForEntity(uri, new HttpEntity<>(list.get(1), headers), ConfigDtoResp.class);

        testConfigIds[0] = response1.getBody().getId();
        testConfigIds[1] = response2.getBody().getId();
    }
}
