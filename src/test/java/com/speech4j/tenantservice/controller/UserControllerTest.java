package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.TenantServiceApplication;
import com.speech4j.tenantservice.dto.handler.ResponseMessageDto;
import com.speech4j.tenantservice.dto.request.UserDtoReq;
import com.speech4j.tenantservice.dto.response.UserDtoResp;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends AbstractContainerBaseTest {
    @Autowired
    private TestRestTemplate template;

    private HttpHeaders headers;
    private HttpEntity<UserDtoReq> request;
    private UserDtoReq testUser;

    private final String exceptionMessage = "User not found!";
    private String  testId;

    @BeforeEach
    void setUp() throws URISyntaxException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Initializing of test user
        testUser = new UserDtoReq();
        testUser.setFirstName("Mar");
        testUser.setLastName("Slob");
        testUser.setEmail("email@gmail.com");
        testUser.setPassword("string123");

        request = new HttpEntity<>(testUser, headers);

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
        ResponseEntity<UserDtoResp> response
                = template.exchange("/tenants/users/" + testId, HttpMethod.GET, request, UserDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void findByIdTest__unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange("/tenants/users/0", HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void addEntityTest_successFlow() {
        final String url = "/tenants/users";

        ResponseEntity<UserDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, UserDtoResp.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void addEntityTest_unsuccessFlow() {
        final String url = "/tenants/users";

        //Make entity null
        request = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void updateEntityTest_successFlow() {
        final String url = "/tenants/users/" + testId;

        testUser.setFirstName("NewName");
        request = new HttpEntity<>(testUser, headers);

        ResponseEntity<UserDtoResp> response =
                this.template.exchange(url, HttpMethod.PUT, request, UserDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(testUser).isEqualToIgnoringGivenFields(response.getBody(),
                "createdDate", "updatedDate", "password" , "role" );
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void updateEntityTest_unsuccessFlow() {
        final String url = "/tenants/users/" + 0;

        testUser.setFirstName("NewName");
        request = new HttpEntity<>(testUser, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void deleteEntity_successFlow() {
        final String url = "/tenants/users/" + testId;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());

    }

    @Test
    public void deleteEntity_unsuccessFlow() {
        final String url = "/tenants/users/" + 0;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void findAllTest() {
        request = new HttpEntity<>(headers);
        ResponseEntity<List> response = template.exchange("/tenants/users", HttpMethod.GET, request, List.class);

        //Checking if status code is correct
        assertEquals(200, response.getStatusCodeValue());
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response){
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    private void populateDB() throws URISyntaxException {
        final String url = "/tenants/users";
        URI uri = new URI(url);

        //entity1
        UserDtoReq user1 = new UserDtoReq();
        user1.setFirstName("Name1");
        user1.setLastName("Surname1");
        user1.setEmail("email@gmail.com");
        user1.setPassword("qwerty123");

        //entity2
        UserDtoReq user2 = new UserDtoReq();
        user2.setFirstName("Name2");
        user2.setLastName("Surname2");
        user2.setEmail("email@gmail.com");
        user2.setPassword("qwerty123");

        ResponseEntity<UserDtoResp> response1 = template.postForEntity(uri, new HttpEntity<>(user1, headers), UserDtoResp.class);
        ResponseEntity<UserDtoResp> response2 = template.postForEntity(uri, new HttpEntity<>(user2, headers), UserDtoResp.class);
        testId = response1.getBody().getId();
    }
}
