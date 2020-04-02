package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.AbstractContainerBaseTest;
import com.speech4j.tenantservice.TenantServiceApplication;
import com.speech4j.tenantservice.dto.handler.ResponseMessageDto;
import com.speech4j.tenantservice.dto.request.TenantDtoReq;
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

import static com.speech4j.tenantservice.util.DataUtil.getListOfTenants;
import static com.speech4j.tenantservice.util.DataUtil.getListOfUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

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
    private String []testTenantId;
    private List<UserDtoReq> usersList;
    private List<TenantDtoReq> tenantsList;

    @BeforeEach
    void setUp() throws URISyntaxException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Initializing of test user
        testUser = new UserDtoReq();
        testUser.setFirstName("Mar");
        testUser.setLastName("Slob");
        testUser.setEmail("email@gmail.com");
        testUser.setPassword("strinG123");

        request = new HttpEntity<>(testUser, headers);

        //Populating of db
        tenantsList = getListOfTenants();
        testTenantId = new TenantControllerTest().populateDB(template, headers,tenantsList);
        usersList = getListOfUsers();
        populateDB(usersList);
    }

    @Test
    public void findByIdTest_successFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<UserDtoResp> response
                = template.exchange("/tenants/" + testTenantId[0] + "/users/" + testId, HttpMethod.GET, request, UserDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void findByIdTest__unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange("/tenants/" + testTenantId[0] + "/users/" + 0, HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void findByIdTestDifferentTenantId_unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange("/tenants/" + testTenantId[1] + "/users/" + testId, HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void createEntityTest_successFlow() {
        final String url = "/tenants/" + testTenantId[0] + "/users/";

        ResponseEntity<UserDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, UserDtoResp.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void createEntityTestWithOptionalField_successFlow() {
        final String url = "/tenants/" + testTenantId[0] + "/users/";

        testUser.setRole(null);
        ResponseEntity<UserDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, UserDtoResp.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void createEntityTest_unsuccessFlow() {
        final String url = "/tenants/" + testTenantId[0] + "/users/";

        //Make entity null
        request = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createEntityTestWithWrongEmail_unsuccessFlow() {
        final String url = "/tenants/" + testTenantId[0] + "/users/";

        testUser.setEmail("wrong-email");
        request = new HttpEntity<>(testUser, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation wrong email
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Validation failed for object='userDtoReq'. Error count: 1", response.getBody().getMessage());
    }

    @Test
    public void createEntityTestWithMissedRequiredField_unsuccessFlow() {
        final String url = "/tenants/" + testTenantId[0] + "/users/";

        testUser.setFirstName(null);
        request = new HttpEntity<>(testUser, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation missed field
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Validation failed for object='userDtoReq'. Error count: 1", response.getBody().getMessage());
    }

    @Test
    public void updateEntityTest_successFlow() {
        final String url = "/tenants/"+testTenantId[0]+"/users/" + testId;

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
        final String url = "/tenants/"+testTenantId[0]+"/users/" + 0;

        testUser.setFirstName("NewName");
        request = new HttpEntity<>(testUser, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void deleteEntity_successFlow() {
        final String url = "/tenants/"+testTenantId[0]+"/users/" + testId;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());

    }

    @Test
    public void deleteEntity_unsuccessFlow() {
        final String url = "/tenants/"+testTenantId[0]+"/users/" + 0;

        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    public void findAllTestByTenantId_successFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<List> response = template.exchange("/tenants/" + testTenantId[0] + "/users", HttpMethod.GET, request, List.class);
       // System.out.println(response);

        //Checking if status code is correct
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usersList.size(), response.getBody().size());
    }

    @Test
    public void findAllTestByTenantId_unsuccessFlow() {
        request = new HttpEntity<>(headers);
        ResponseEntity<ResponseMessageDto> response = template.exchange("/tenants/" + 0 + "/users", HttpMethod.GET, request, ResponseMessageDto.class);
        System.out.println(response);

        //Checking if status code is correct
        checkEntityNotFoundException(response);
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response){
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    private void populateDB(List<UserDtoReq> list) throws URISyntaxException {
        final String url = "/tenants/" + testTenantId[0] + "/users";
        URI uri = new URI(url);

        ResponseEntity<UserDtoResp> response1 = template.postForEntity(uri, new HttpEntity<>(list.get(0), headers), UserDtoResp.class);
        ResponseEntity<UserDtoResp> response2 = template.postForEntity(uri, new HttpEntity<>(list.get(1), headers), UserDtoResp.class);
        testId = response1.getBody().getId();
    }
}
