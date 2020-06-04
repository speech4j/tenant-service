package org.speech4j.tenantservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.speech4j.tenantservice.AbstractContainer;
import org.speech4j.tenantservice.TenantServiceApplication;
import org.speech4j.tenantservice.dto.request.ConfigDtoReq;
import org.speech4j.tenantservice.dto.response.ConfigDtoResp;
import org.speech4j.tenantservice.dto.response.ResponseMessageDto;
import org.speech4j.tenantservice.entity.tenant.ApiName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.speech4j.tenantservice.fixture.DataFixture.getListOfConfigs;

@Slf4j
@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigControllerTest extends AbstractContainer {
    @Autowired
    private TestRestTemplate template;
    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity<ConfigDtoReq> request;
    private final String exceptionMessage = "Config not found!";

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findByIdTest_successFlow(
            Map<String, Map<String, Object>> data
    ){
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/configs/" + data.get("configIds").get("real-1");
        ResponseEntity<ConfigDtoResp> response
                = template.exchange(url, HttpMethod.GET, null, ConfigDtoResp.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(data.get("dtos").get("response")).isEqualToIgnoringGivenFields(response.getBody(), "tenantId", "credentials");
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findByIdTest_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/configs/" + data.get("configIds").get("fake");
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.GET, null, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findByIdTestDifferentTenantId_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-2") + "/configs/" + data.get("configIds").get("real-1");
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.GET, null, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createConfigTest_successFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/configs";
        ConfigDtoReq requestDto = (ConfigDtoReq) data.get("dtos").get("request");
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<ConfigDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, ConfigDtoResp.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(), "id", "credentials");
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createConfigTest_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/configs";
        //Make entity null
        request = new HttpEntity<>(null, headers);
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(415, response.getStatusCodeValue());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void updateConfigTest_successFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/configs/" + data.get("configIds").get("real-1");
        ConfigDtoReq requestDto = (ConfigDtoReq) data.get("dtos").get("request");
        requestDto.setApiName(ApiName.HEROKU);
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<ConfigDtoReq> response =
                this.template.exchange(url, HttpMethod.PUT, request, ConfigDtoReq.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(requestDto, response.getBody());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void updateConfigTest_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/configs/" + data.get("configIds").get("fake");
        ConfigDtoReq requestDto = (ConfigDtoReq) data.get("dtos").get("request");
        requestDto.setApiName(ApiName.HEROKU);
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void deleteConfig_successFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/configs/" + data.get("configIds").get("real-2");
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, null, ResponseMessageDto.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void deleteConfig_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/configs/" + data.get("configIds").get("fake");
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, null, ResponseMessageDto.class);

        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findAllConfigsTest_successFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("real-1") + "/configs";
        ResponseEntity<List<ConfigDtoResp>> response = template.exchange(url,
                HttpMethod.GET, request, new ParameterizedTypeReference<List<ConfigDtoResp>>(){});

        //Checking if status code is correct
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findAllConfigsTestByTenantId_unsuccessFlow(
            Map<String, Map<String, Object>> data
    ) {
        final String url = "/tenants/" + data.get("tenantIds").get("fake") + "/configs";
        ResponseEntity<ResponseMessageDto> response = template.exchange(url, HttpMethod.GET, request, ResponseMessageDto.class);

        //Checking if status code is correct
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Tenant with specified identifier [0] not found!", response.getBody().getMessage());
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response){
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    private static Stream<Arguments> provideTestData() {
        Map<String,Object> credentials = new HashMap<>();
        credentials.put("username", "mslob");
        credentials.put("password", "qwerty123");
        //entity1
        ConfigDtoReq request = new ConfigDtoReq();
        request.setApiName(ApiName.GOOGLE);
        request.setCredentials(credentials);

        Map<String, Object> dtos = new HashMap<>();
        dtos.put("request", request);
        dtos.put("response",getListOfConfigs().get(0));
        dtos.put("null", null);
        Map<String, Object> tenantIds = new HashMap();
        tenantIds.put("real-1", "test_tenant_1");
        tenantIds.put("real-2", "test_tenant_2");
        tenantIds.put("fake", "0");
        Map<String, Object> configIds = new HashMap();
        configIds.put("real-1", "1");
        configIds.put("real-2", "2");
        configIds.put("fake", "0");

        Map<String, Map<String, Object>> data = new HashMap<>();
        data.put("tenantIds", tenantIds);
        data.put("configIds", configIds);
        data.put("dtos", dtos);
        return Stream.of(
                Arguments.of(data)
        );
    }
}
