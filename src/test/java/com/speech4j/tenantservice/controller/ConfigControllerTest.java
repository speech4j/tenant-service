package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.AbstractContainerBaseTest;
import com.speech4j.tenantservice.TenantServiceApplication;
import com.speech4j.tenantservice.dto.handler.ResponseMessageDto;
import com.speech4j.tenantservice.dto.request.ConfigDtoReq;
import com.speech4j.tenantservice.dto.response.ConfigDtoResp;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigControllerTest extends AbstractContainerBaseTest {
    @Autowired
    private TestRestTemplate template;

    private HttpHeaders headers;
    private HttpEntity<ConfigDtoReq> request;
    private ConfigDtoReq testConfig;

    private final String exceptionMessage = "Config not found!";
    private String testId;
    private String testTenantId;

    @BeforeEach
    void setUp() throws URISyntaxException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Initializing of test com.speech4j.tenantservice.config
        testConfig = new ConfigDtoReq();
        testConfig.setApiName("Azure Api");
        testConfig.setUsername("testName");
        testConfig.setPassword("qwerty123");

        request = new HttpEntity<>(testConfig, headers);

        //Populating of db
        testTenantId = new TenantControllerTest().populateDB(template, headers);
        populateDB();
    }

    @Test
    public void findByIdTest_successFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<TenantDtoResp> response
                = template.exchange("/tenants/" + testTenantId + "/configs/" + testId, HttpMethod.GET, request, TenantDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void findByIdTest__unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange("/tenants/" + testTenantId + "/configs/0", HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void addEntityTest_successFlow() {
        final String url = "/tenants/" + testTenantId + "/configs";

        ResponseEntity<ConfigDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, ConfigDtoResp.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void addEntityTest_unsuccessFlow() {
        final String url = "/tenants/" + testTenantId + "/configs";

        //Make entity null
        request = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void updateEntityTest_successFlow() {
        final String url = "/tenants/" + testTenantId + "/configs/" + testId;

        testConfig.setApiName("newName");
        request = new HttpEntity<>(testConfig, headers);

        ResponseEntity<ConfigDtoReq> response =
                this.template.exchange(url, HttpMethod.PUT, request, ConfigDtoReq.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testConfig, response.getBody());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void updateEntityTest_unsuccessFlow() {
        final String url = "/tenants/" + testTenantId + "/configs/" + 0;

        testConfig.setApiName("newName");
        request = new HttpEntity<>(testConfig, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void deleteEntity_successFlow() {
        final String url = "/tenants/" + testTenantId + "/configs/" + testId;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());

    }

    @Test
    public void deleteEntity_unsuccessFlow() {
        final String url = "/tenants/" + testTenantId + "/configs/" + 0l;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void findAllTest() {
        request = new HttpEntity<>(headers);
        ResponseEntity<List> response = template.exchange("/tenants/" + testTenantId + "/configs/",
                HttpMethod.GET, request, List.class);

        //Checking if status code is correct
        assertEquals(200, response.getStatusCodeValue());
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response){
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    private void populateDB() throws URISyntaxException {
        final String url = "/tenants/" + testTenantId + "/configs/";
        URI uri = new URI(url);

        //entity1
        ConfigDtoReq config1 = new ConfigDtoReq();
        config1.setApiName("Google Api");
        config1.setUsername("mslob");
        config1.setPassword("qwerty123");

        //entity2
        ConfigDtoReq config2 = new ConfigDtoReq();
        config2.setApiName("AWS Api");
        config2.setUsername("speech4j");
        config2.setPassword("qwerty123");

        ResponseEntity<ConfigDtoResp> response1 = template.postForEntity(uri, new HttpEntity<>(config1, headers), ConfigDtoResp.class);
        ResponseEntity<ConfigDtoResp> response2 = template.postForEntity(uri, new HttpEntity<>(config2, headers), ConfigDtoResp.class);

        testId = response1.getBody().getId();
    }

}
