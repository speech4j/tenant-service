package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.ConfigDto;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

class FirstTest extends AbstractContainerBaseTest {
    @LocalServerPort
    private int port;
    private TestRestTemplate template;
    private String baseUrl;

    private HttpHeaders headers;
    private HttpEntity<ConfigDto> request;
    private ConfigDto testConfig;


    @BeforeEach
    void setUp() {
        template = new TestRestTemplate();
        baseUrl = "http://localhost:" + port;

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Initializing of test config
        testConfig = new ConfigDto();
        testConfig.setApiName("Azure Api");
        testConfig.setUsername("testName");
        testConfig.setPassword("qwerty123");

        request = new HttpEntity<>(testConfig, headers);
    }

    @Test
    void isRunningContainer() throws URISyntaxException {
        assertTrue(postgreSQLContainer.isRunning());

        //Populating of db
        populateDB();
    }

    @Test
    public void findByIdTest_successFlow() {
        ConfigDto response =
                this.template.getForObject(baseUrl + "/tenants/users/configs/1", ConfigDto.class);
        assertNotNull(response);
    }

    @Test
    public void findByIdTest__unsuccessFlow() {
        ResponseMessageDto response =
                this.template.getForObject(baseUrl + "/tenants/users/configs/100", ResponseMessageDto.class);
        assertEquals("Config not found!", response.getMessage());
    }

    @Test
    public void addEntityTest_successFlow() {
        final String url = baseUrl + "/tenants/users/configs";

        ResponseEntity<ConfigDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ConfigDto.class);

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
        final String url = baseUrl + "/tenants/users/configs/me";

        testConfig.setId(1l);
        testConfig.setApiName("newName");
        request = new HttpEntity<>(testConfig, headers);

        ResponseEntity<ConfigDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ConfigDto.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testConfig, response.getBody());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void updateEntityTest_unsuccessFlow() {
        final String url = baseUrl + "/tenants/users/configs/me";

        testConfig.setId(100l);
        testConfig.setApiName("newName");
        request = new HttpEntity<>(testConfig, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Config not found!", response.getBody().getMessage());
    }

    @Test
    public void deleteEntity_successFlow() {
        Long id = 2l;
        final String url = baseUrl + "/tenants/users/configs/" + id;

        request = new HttpEntity<ConfigDto>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());

    }

    @Test
    public void deleteEntity_unsuccessFlow() {
        Long id = 100l;
        final String url = baseUrl + "/tenants/users/configs/" + id;

        request = new HttpEntity<ConfigDto>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Verify request not succeed
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Config not found!", response.getBody().getMessage());
    }

    @Test
    public void findAllTest() {
        List<ConfigDto> response = this.template.getForObject(baseUrl + "/tenants/users/configs", List.class);
        assertEquals(1, response.size());
    }


    private void populateDB() throws URISyntaxException {
        final String url = baseUrl + "/tenants/users/configs";
        URI uri = new URI(url);

        //entity1
        ConfigDto config1 = new ConfigDto();
        config1.setApiName("Google Api");
        config1.setUsername("mslob");
        config1.setPassword("qwerty123");

        //entity2
        ConfigDto config2 = new ConfigDto();
        config2.setApiName("AWS Api");
        config2.setUsername("speech4j");
        config2.setPassword("qwerty123");

        template.postForEntity(uri, new HttpEntity<>(config1, headers), ConfigDto.class);
        template.postForEntity(uri, new HttpEntity<>(config2, headers), ConfigDto.class);
    }

}
