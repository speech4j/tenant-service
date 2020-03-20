package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.controller.AbstractContainerBaseTest;
import com.speech4j.tenantservice.dto.ConfigDto;
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
import static org.junit.Assert.assertNull;
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
    void setUp() throws URISyntaxException {
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

        //Populating of db
        populateDB();
    }

    @Test
    void isRunningContainer() {
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    public void findByIdTest(){
        ConfigDto response = this.template.getForObject(baseUrl + "/configs/2", ConfigDto.class);
        assertNotNull(response.getId());
    }

    @Test
    public void findAllTest(){
        List<ConfigDto> response = this.template.getForObject(baseUrl + "/configs", List.class);
        System.out.println("Get All users: " + response);
        assertEquals(4, response.size());
    }

    @Test
    public void addEntityTest() {
        final String url = baseUrl + "/configs";

        ResponseEntity<ConfigDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ConfigDto.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void updateEntityTest() {
        final String url = baseUrl + "/configs/me";

        testConfig.setId(2l);
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
    public void deleteEntity(){
        Long id = 2l;
        final String url = baseUrl + "configs/" + id;

        template.delete(url);

        //checking if entity was deleted
        ConfigDto response = this.template.getForObject(baseUrl + "/configs/" + id, ConfigDto.class);
        assertNull(response.getId());
        //TODO: check if throw IllegalArgumentException
    }


    private void populateDB() throws URISyntaxException {
        final String url = baseUrl + "/configs";
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
