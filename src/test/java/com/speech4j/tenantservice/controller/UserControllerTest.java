package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.UserDto;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest extends AbstractContainerBaseTest {
    @LocalServerPort
    private int port;
    private TestRestTemplate template;
    private String baseUrl;

    private HttpHeaders headers;
    private HttpEntity<UserDto> request;
    private UserDto testUser;

    private final String exceptionMessage = "User not found!";
    private Long testId;

    @BeforeEach
    void setUp() throws URISyntaxException {
        template = new TestRestTemplate();
        baseUrl = "http://localhost:" + port;

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Initializing of test user
        testUser = new UserDto();
        testUser.setFirstName("Mar");
        testUser.setLastName("Slob");
        testUser.setEmail("email@gmail.com");
        testUser.setPassword("string123");
        testUser.setActive(true);

        request = new HttpEntity<>(testUser, headers);

        //Populating of db
        populateDB();
    }

    @Test
    void isRunningContainer(){
        postgreSQLContainer.start();
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    public void findByIdTest_successFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<UserDto> response
                = template.exchange(baseUrl + "/tenants/users/" + testId, HttpMethod.GET, request, UserDto.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void findByIdTest__unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(baseUrl + "/tenants/users/0", HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void addEntityTest_successFlow() {
        final String url = baseUrl + "/tenants/users";

        ResponseEntity<UserDto> response =
                this.template.exchange(url, HttpMethod.POST, request, UserDto.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void addEntityTest_unsuccessFlow() {
        final String url = baseUrl + "/tenants/users";

        //Make entity null
        request = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void updateEntityTest_successFlow() {
        final String url = baseUrl + "/tenants/users/me";

        testUser.setId(testId);
        testUser.setFirstName("NewName");
        request = new HttpEntity<>(testUser, headers);

        ResponseEntity<UserDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, UserDto.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(testUser).isEqualToIgnoringGivenFields(response.getBody(), "createdDate", "updatedDate", "password" );
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void updateEntityTest_unsuccessFlow() {
        final String url = baseUrl + "/tenants/users/me";

        testUser.setId(0l);
        testUser.setFirstName("NewName");
        request = new HttpEntity<>(testUser, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void deleteEntity_successFlow() {
        final String url = baseUrl + "/tenants/users/" + testId;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());

    }

    @Test
    public void deleteEntity_unsuccessFlow() {
        final String url = baseUrl + "/tenants/users/" + 0;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void findAllTest() {
        request = new HttpEntity<>(headers);
        ResponseEntity<List> response = template.exchange(baseUrl + "/tenants/users", HttpMethod.GET, request, List.class);

        //Checking if status code is correct
        assertEquals(200, response.getStatusCodeValue());
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response){
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    private void populateDB() throws URISyntaxException {
        final String url = baseUrl + "/tenants/users";
        URI uri = new URI(url);

        //entity1
        UserDto user1 = new UserDto();
        user1.setFirstName("Name1");
        user1.setLastName("Surname1");
        user1.setEmail("email@gmail.com");
        user1.setPassword("qwerty123");
        user1.setActive(true);

        //entity2
        UserDto user2 = new UserDto();
        user2.setFirstName("Name2");
        user2.setLastName("Surname2");
        user2.setEmail("email@gmail.com");
        user2.setPassword("qwerty123");
        user2.setActive(true);

        ResponseEntity<UserDto> response1 = template.postForEntity(uri, new HttpEntity<>(user1, headers), UserDto.class);
        ResponseEntity<UserDto> response2 = template.postForEntity(uri, new HttpEntity<>(user2, headers), UserDto.class);
        testId = response1.getBody().getId();
    }
}
