package org.speech4j.tenantservice.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.speech4j.tenantservice.AbstractContainer;
import org.speech4j.tenantservice.TenantServiceApplication;
import org.speech4j.tenantservice.dto.request.TenantDtoCreateReq;
import org.speech4j.tenantservice.dto.request.TenantDtoUpdateReq;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.speech4j.tenantservice.fixture.DataFixture.getListOfTenants;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TenantControllerTest
         extends AbstractContainer
{
    private WebTestClient testClient;

    private List<TenantDtoResp> expectedList;

    @Autowired
    private TenantService service;

    @LocalServerPort
    private int port;

    @BeforeEach
    void beforeEach() {
        this.testClient =
                WebTestClient.bindToServer()
                        .baseUrl("http://localhost:" + port + "/tenants")
                        .build();

        this.expectedList =
                service.getTenants().collectList().block();
    }

    @Test
    void testGetAllTenants_successFlow() {
        List<TenantDtoResp> actual = testClient.get()
                .uri("/").exchange()
                .expectStatus().isOk()
                .expectBodyList(TenantDtoResp.class).returnResult().getResponseBody();
        Assertions.assertEquals(expectedList.toString(), actual.toString());
    }

    @Test
    void testGetTenantById_successFlow() {
        TenantDtoResp expectedTenant = expectedList.get(0);
        TenantDtoResp tenant = testClient.get().uri("/{id}", expectedTenant.getId())
                .exchange().expectStatus().isOk().expectBody(TenantDtoResp.class)
                .returnResult().getResponseBody();

        assertEquals(expectedTenant.toString(), tenant.toString());
    }

    @Test
    void testGetTenantInvalidIdNotFound_unSuccessFlow() {
        testClient.get().uri("/fake").exchange().expectStatus().isNotFound();
    }

    @Test
    void testDeleteTenantById_successFlow() {
        TenantDtoResp expectedTenant = expectedList.get(1);
        testClient.delete().uri("/{id}", expectedTenant.getId()).exchange().expectStatus().isOk();
    }

    @Test
    void testDeleteTenantById_unSuccessFlow() {
        testClient.delete().uri("/{id}", "fake").exchange().expectStatus().isNotFound();
    }

    @Test
    public void testCreateTenant_successFlow() {

        TenantDtoCreateReq tenant = getListOfTenants().get(0);
        tenant.setName("new_tenant");

        TenantDtoResp actual = testClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(tenant), TenantDtoCreateReq.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TenantDtoResp.class)
                .returnResult().getResponseBody();

        assertThat(tenant).isEqualToIgnoringGivenFields(actual,
                "id", "createdDate", "modifiedDate", "name");
    }

    @Test
    public void testCreateTenant_unSuccessFlow() {
        TenantDtoCreateReq tenant = getListOfTenants().get(0);
        testClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(tenant), TenantDtoCreateReq.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testCreateTenantWithMissedRequiredField_unSuccessFlow() {
        TenantDtoCreateReq tenant = getListOfTenants().get(0);
        tenant.setDescription(null);

        testClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(tenant), TenantDtoCreateReq.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testCreateTenantWithBlankRequiredField_unSuccessFlow() {
        TenantDtoCreateReq tenant = getListOfTenants().get(0);
        tenant.setDescription(" ");

        testClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(tenant), TenantDtoCreateReq.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testUpdateTenant_successFlow() {
        TenantDtoCreateReq tenant = getListOfTenants().get(0);
        TenantDtoUpdateReq tenantDtoUpdateReq = new TenantDtoUpdateReq();
        tenantDtoUpdateReq.setDescription("New description!");

        TenantDtoResp actual = testClient.put()
                .uri("/{id}", tenant.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(tenantDtoUpdateReq), TenantDtoUpdateReq.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TenantDtoResp.class)
                .returnResult().getResponseBody();


        assertThat(tenantDtoUpdateReq).isEqualToIgnoringGivenFields(actual,
                "id", "createdDate", "modifiedDate", "name");
    }

    @Test
    public void testUpdateTenant_unSuccessFlow() {
        TenantDtoUpdateReq tenantDtoUpdateReq = new TenantDtoUpdateReq();
        tenantDtoUpdateReq.setDescription("New description!");

        testClient.put()
                .uri("/{id}", "fake")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(tenantDtoUpdateReq), TenantDtoUpdateReq.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}