package org.speech4j.tenantservice.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.speech4j.tenantservice.AbstractContainer;
import org.speech4j.tenantservice.TenantServiceApplication;
import org.speech4j.tenantservice.dto.request.UserDtoReq;
import org.speech4j.tenantservice.dto.response.ResponseMessageDto;
import org.speech4j.tenantservice.dto.response.UserDtoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.speech4j.tenantservice.fixture.DataFixture.getListOfUsers;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest extends AbstractContainer {
    @Autowired
    private TestRestTemplate template;
    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity<UserDtoReq> request;
    private final String exceptionMessage = "User not found!";

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findUserByIdTest_successFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users/" + data.get("userIds").get("real-1");
        ResponseEntity<UserDtoResp> response
                = template.exchange(url, HttpMethod.GET, null, UserDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(data.get("dtos").get("response")).isEqualToIgnoringGivenFields(response.getBody(),
                "createdDate", "modifiedDate", "role", "active");
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findUserByIdTest_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users/" + data.get("userIds").get("fake");
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.GET, null, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findUserByIdTestDifferentTenantId_unsuccessFlow(
            Map<String, Map<String, Object>> data

    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-2") + "/users/" + data.get("userIds").get("real-1");
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.GET, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createUserTest_successFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users";
        UserDtoReq requestDto = (UserDtoReq) data.get("dtos").get("request");
        requestDto.setEmail("email@gmail.com");
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<UserDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, UserDtoResp.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(),
                "id", "createdDate", "modifiedDate", "password", "role");
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createUserTestWithOptionalField_successFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users";
        UserDtoReq requestDto = (UserDtoReq) data.get("dtos").get("request");
        requestDto.setRole(null);
        requestDto.setEmail("emaill@gmail.com");
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<UserDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, UserDtoResp.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(),
                "id", "createdDate", "modifiedDate", "password", "role");
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createUserTest_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users";
        request = new HttpEntity(data.get("dtos").get("null"), headers);
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(415, response.getStatusCodeValue());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createUserTestWithWrongEmail_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users";

        UserDtoReq requestDto = (UserDtoReq) data.get("dtos").get("request");
        requestDto.setEmail("wrong-email");
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation wrong email
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Validation failed for object='userDtoReq'. Error count: 1", response.getBody().getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createUserTestWithMissedRequiredField_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users";
        UserDtoReq requestDto = (UserDtoReq) data.get("dtos").get("request");
        requestDto.setFirstName(null);
        requestDto.setEmail("email@gmail.com");
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation missed field
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Validation failed for object='userDtoReq'. Error count: 1", response.getBody().getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createUserTestWithDuplicateEmail_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users";
        UserDtoReq requestDto = (UserDtoReq) data.get("dtos").get("request");
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation wrong email
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("User with a specified email already exists!",
                response.getBody().getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void updateUserTest_successFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users/" + data.get("userIds").get("real-1");
        UserDtoReq requestDto = (UserDtoReq) data.get("dtos").get("request");
        requestDto.setFirstName("New Name");
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<UserDtoResp> response =
                this.template.exchange(url, HttpMethod.PUT, request, UserDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(),
                "createdDate", "updatedDate", "password", "role");
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void updateUserTest_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users/" + data.get("userIds").get("fake");
        UserDtoReq requestDto = (UserDtoReq) data.get("dtos").get("request");
        requestDto.setFirstName("New Name");
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void deleteUser_successFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users/" + data.get("userIds").get("real-2");
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, null, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void deleteUser_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users/" + data.get("userIds").get("fake");
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, null, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findAllUsersTestByTenantId_successFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/users";
        ResponseEntity<List<UserDtoResp>> response =
                template.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDtoResp>>() {
                });

        //Checking if status code is correct
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findAllUsersTestByTenantId_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("fake") + "/users";
        ResponseEntity<ResponseMessageDto> response =
                template.exchange(url, HttpMethod.GET, null, ResponseMessageDto.class);

        //Checking if status code is correct
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Tenant with specified identifier [" + data.get("tenantIds").get("fake") + "] not found!",
                response.getBody().getMessage());
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response) {
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }


    private static Stream<Arguments> provideTestData() throws URISyntaxException {
        UserDtoResp response = new UserDtoResp();
        response.setId("1");
        response.setFirstName("Name1");
        response.setLastName("Surname1");
        response.setEmail("email1@gmail.com");

        Map<String, Object> dtos = new HashMap<>();
        dtos.put("request", getListOfUsers().get(0));
        dtos.put("response", response);
        dtos.put("null", null);
        Map<String, Object> tenantIds = new HashMap();
        tenantIds.put("real-1", "test_tenant_1");
        tenantIds.put("real-2", "test_tenant_2");
        tenantIds.put("fake", "0");
        Map<String, Object> userIds = new HashMap();
        userIds.put("real-1", "1");
        userIds.put("real-2", "2");
        userIds.put("fake", "0");
        Map<String, Map<String, Object>> data = new HashMap<>();
        data.put("tenantIds", tenantIds);
        data.put("userIds", userIds);
        data.put("dtos", dtos);

        return Stream.of(
                Arguments.of(data)
        );
    }
}
