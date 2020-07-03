package org.speech4j.tenantservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.speech4j.tenantservice.AbstractContainer;
import org.speech4j.tenantservice.TenantServiceApplication;
import org.speech4j.tenantservice.dto.request.ConfigDtoReq;
import org.speech4j.tenantservice.dto.response.ConfigDtoResp;
import org.speech4j.tenantservice.entity.tenant.ApiName;
import org.speech4j.tenantservice.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.speech4j.tenantservice.fixture.DataFixture.getListOfTenants;


@Slf4j
@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigControllerTest
        extends AbstractContainer
{
    private WebTestClient testClient;

    private List<ConfigDtoResp> expectedConfigs;

    private String expectedTenantId;

    @Autowired
    private ConfigService configService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void beforeEach() {
        this.testClient =
                WebTestClient.bindToServer()
                        .baseUrl("http://localhost:" + port + "/tenants")
                        .build();

        this.expectedTenantId = getListOfTenants().get(0).getName();
        this.expectedConfigs = configService.getAllById(expectedTenantId).collectList().block();
    }

    @Test
    void testGetAllConfigs_successFlow() {
        List<ConfigDtoResp> actual = testClient.get()
                .uri("/{tenantId}/configs", expectedTenantId).exchange()
                .expectStatus().isOk()
                .expectBodyList(ConfigDtoResp.class)
                .returnResult().getResponseBody();
        Assertions.assertEquals(expectedConfigs.toString(), actual.toString());
    }

    @Test
    void testGetAllConfigs_unSuccessFlow() {
        testClient.get().uri("/{tenantId}/configs", "fake")
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void testGetConfigById_successFlow() {
        ConfigDtoResp expectedConfig = expectedConfigs.get(0);
        ConfigDtoResp config = testClient.get()
                .uri("/{tenantId}/configs/{configId}", expectedTenantId, expectedConfig.getId())
                .exchange().expectStatus().isOk()
                .expectBody(ConfigDtoResp.class)
                .returnResult().getResponseBody();

        Assertions.assertEquals(config.toString(), expectedConfig.toString());
    }

    @Test
    void testGetConfigById_unSuccessFlow() {
        testClient.get().uri("/{tenantId}/configs/{configId}", expectedTenantId, "fake")
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void testGetConfigByIdWithWrongTenantId_unSuccessFlow() {
        ConfigDtoResp expectedConfig = expectedConfigs.get(0);
        testClient.get().uri("/{tenantId}/configs/{configId}", getListOfTenants().get(1).getName() , expectedConfig.getId())
                .exchange().expectStatus().isNotFound();
    }


    @Test
    void testDeleteConfigById_successFlow() {
        ConfigDtoResp expectedConfig = expectedConfigs.get(0);
        testClient.delete()
                .uri("/{tenantId}/configs/{configId}", expectedTenantId, expectedConfig.getId())
                .exchange().expectStatus().isOk();
    }

    @Test
    void testDeleteConfigById_unSuccessFlow() {
        testClient.delete()
                .uri("/{tenantId}/configs/{configId}", expectedTenantId, "fake")
                .exchange().expectStatus().isNotFound();
    }

    @Test
    public void testCreateConfig_successFlow() {
        ConfigDtoReq config = new ConfigDtoReq();
        config.setApiName(expectedConfigs.get(0).getApiName());
        config.setCredentials(expectedConfigs.get(0).getCredentials());

        ConfigDtoResp actual = testClient.post()
                .uri("/{tenantId}/configs", expectedTenantId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(config), ConfigDtoReq.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ConfigDtoResp.class)
                .returnResult().getResponseBody();

        assertThat(config).isEqualToIgnoringGivenFields(actual, "id");
    }

    @Test
    public void testCreateConfig_unSuccessFlow() {
        ConfigDtoReq config = new ConfigDtoReq();
        config.setApiName(expectedConfigs.get(0).getApiName());
        config.setCredentials(expectedConfigs.get(0).getCredentials());

        testClient.post()
                .uri("/{tenantId}/configs", "fake")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(config), ConfigDtoReq.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void tesUpdateConfig_successFlow() {
        ConfigDtoReq expectedConfig = new ConfigDtoReq();
        expectedConfig.setApiName(ApiName.HEROKU);
        expectedConfig.setCredentials(expectedConfigs.get(0).getCredentials());

        ConfigDtoResp actual = testClient.put()
                .uri("/{tenantId}/configs/{configId}", expectedTenantId, expectedConfigs.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(expectedConfig), ConfigDtoReq.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConfigDtoResp.class)
                .returnResult().getResponseBody();

        assertThat(expectedConfig).isEqualToIgnoringGivenFields(actual, "id");
    }

    @Test
    public void testUpdateConfig_unSuccessFlow() {
        ConfigDtoResp expectedConfig = expectedConfigs.get(0);
        expectedConfig.setApiName(ApiName.HEROKU);

        testClient.put()
                .uri("/{tenantId}/configs/{configId}", expectedTenantId, "fake")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(expectedConfig), ConfigDtoReq.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}