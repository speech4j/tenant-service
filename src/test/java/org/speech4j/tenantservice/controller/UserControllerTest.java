package org.speech4j.tenantservice.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.speech4j.tenantservice.TenantServiceApplication;
import org.speech4j.tenantservice.dto.request.UserDtoReq;
import org.speech4j.tenantservice.dto.response.UserDtoResp;
import org.speech4j.tenantservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.speech4j.tenantservice.fixture.DataFixture.getListOfTenants;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest
        //extends AbstractContainer
{
    private WebTestClient testClient;

    private List<UserDtoResp> expectedUsers;

    private String expectedTenantId;

    @Autowired
    private UserService userService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void beforeEach() {
        this.testClient =
                WebTestClient.bindToServer()
                        .baseUrl("http://localhost:" + port + "/tenants")
                        .build();

        this.expectedTenantId = getListOfTenants().get(0).getName();
        this.expectedUsers = userService.getAllById(expectedTenantId).collectList().block();
    }

    @Test
    void testGetAllUsers_successFlow() {
        List<UserDtoResp> actual = testClient.get()
                .uri("/{tenantId}/users", expectedTenantId).exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDtoResp.class)
                .returnResult().getResponseBody();
        Assertions.assertEquals(expectedUsers.toString(), actual.toString());
    }

    @Test
    void testGetAllUsers_unSuccessFlow() {
        testClient.get().uri("/{tenantId}/users", "fake")
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void testGetUserById_successFlow() {
        UserDtoResp expectedUser = expectedUsers.get(0);
        UserDtoResp config = testClient.get()
                .uri("/{tenantId}/users/{userId}", expectedTenantId, expectedUser.getId())
                .exchange().expectStatus().isOk()
                .expectBody(UserDtoResp.class)
                .returnResult().getResponseBody();

        Assertions.assertEquals(config.toString(), expectedUser.toString());
    }

    @Test
    void testGetUserById_unSuccessFlow() {
        testClient.get().uri("/{tenantId}/users/{userId}", expectedTenantId, "fake")
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void testGetUserByIdWithWrongTenantId_unSuccessFlow() {
        UserDtoResp expectedUser = expectedUsers.get(0);
        testClient.get().uri("/{tenantId}/users/{userId}", getListOfTenants().get(1).getName(), expectedUser.getId())
                .exchange().expectStatus().isNotFound();
    }


    @Test
    void testDeleteUserById_successFlow() {
        UserDtoResp expectedUser = expectedUsers.get(1);
        testClient.delete()
                .uri("/{tenantId}/users/{userId}", expectedTenantId, expectedUser.getId())
                .exchange().expectStatus().isOk();
    }

    @Test
    void testDeleteUserById_unSuccessFlow() {
        testClient.delete()
                .uri("/{tenantId}/users/{userId}", expectedTenantId, "fake")
                .exchange().expectStatus().isNotFound();
    }

    @Test
    public void testCreateUser_successFlow() {
        UserDtoResp expectedUser = expectedUsers.get(0);
        UserDtoReq userDtoReq = new UserDtoReq();
        userDtoReq.setFirstName(expectedUser.getFirstName());
        userDtoReq.setLastName(expectedUser.getLastName());
        userDtoReq.setEmail(expectedUser.getEmail());
        userDtoReq.setPassword("Qwerty123");

        UserDtoResp actual = testClient.post()
                .uri("/{tenantId}/users", expectedTenantId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(userDtoReq), UserDtoReq.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserDtoResp.class)
                .returnResult().getResponseBody();

        assertThat(userDtoReq).isEqualToIgnoringGivenFields(actual,
                "id", "createdDate", "modifiedDate", "active", "password", "role");
    }

    @Test
    public void testCreateUser_unSuccessFlow() {
        UserDtoResp expectedUser = expectedUsers.get(0);
        UserDtoReq userDtoReq = new UserDtoReq();
        userDtoReq.setFirstName(expectedUser.getFirstName());
        userDtoReq.setLastName(expectedUser.getLastName());
        userDtoReq.setEmail(expectedUser.getEmail());
        userDtoReq.setPassword("Qwerty123");

        testClient.post()
                .uri("/{tenantId}/users", "fake")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(userDtoReq), UserDtoReq.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void tesUpdateUser_successFlow() {
        UserDtoResp expectedUser = expectedUsers.get(0);
        UserDtoReq userDtoReq = new UserDtoReq();
        userDtoReq.setFirstName("New name");
        userDtoReq.setLastName(expectedUser.getLastName());
        userDtoReq.setEmail(expectedUser.getEmail());
        userDtoReq.setPassword("Qwerty123");
        userDtoReq.setRole(expectedUser.getRole());

        UserDtoResp actual = testClient.put()
                .uri("/{tenantId}/users/{userId}", expectedTenantId, expectedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(userDtoReq), UserDtoReq.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDtoResp.class)
                .returnResult().getResponseBody();

        assertThat(userDtoReq).isEqualToIgnoringGivenFields(actual,
                "id", "createdDate", "modifiedDate", "active", "password");
    }

    @Test
    public void testUpdateUser_unSuccessFlow() {
        UserDtoResp expectedUser = expectedUsers.get(0);
        UserDtoReq userDtoReq = new UserDtoReq();
        userDtoReq.setFirstName("New name");
        userDtoReq.setLastName(expectedUser.getLastName());
        userDtoReq.setEmail(expectedUser.getEmail());
        userDtoReq.setPassword("Qwerty123");

        testClient.put()
                .uri("/{tenantId}/users/{userId}", expectedTenantId, "fake")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(userDtoReq), UserDtoReq.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}