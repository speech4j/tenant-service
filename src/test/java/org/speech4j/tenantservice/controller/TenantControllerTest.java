package org.speech4j.tenantservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.speech4j.tenantservice.AbstractContainer;
import org.speech4j.tenantservice.TenantServiceApplication;
import org.speech4j.tenantservice.dto.request.TenantDtoCreateReq;
import org.speech4j.tenantservice.dto.response.ResponseMessageDto;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;
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
import static org.speech4j.tenantservice.fixture.DataFixture.getListOfTenants;

@SpringBootTest(classes = TenantServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TenantControllerTest extends AbstractContainer {
    @Autowired
    private TestRestTemplate template;
    private static HttpHeaders headers = new HttpHeaders();
    private HttpEntity<TenantDtoCreateReq> request;
    private final String exceptionMessage = "Tenant not found!";

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findTenantByIdTest_successFlow(
            Map<String, Object> dtos,
            Map<String, String> tenantIds
    ) {
        final String url = "/tenants/" + tenantIds.get("real-1");
        ResponseEntity<TenantDtoResp> response
                = template.exchange(url, HttpMethod.GET, null, TenantDtoResp.class);
        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(dtos.get("response")).isEqualToIgnoringGivenFields(response.getBody(),
                "id", "createdDate", "modifiedDate", "name");
        assertThat(response.getBody()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void findTenantByIdTest_unsuccessFlow(
            Map<String, String> tenantIds
    ) {
        final String url = "/tenants/" + tenantIds.get("fake");
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.GET, null, ResponseMessageDto.class);
        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createTenantTest_successFlow(
            Map<String, Object> dtos
    ) {
        final String url = "/tenants";
        TenantDtoCreateReq requestDto = (TenantDtoCreateReq) dtos.get("request");
        requestDto.setName("new_name");
        request = new HttpEntity(requestDto, headers);
        ResponseEntity<TenantDtoResp> response =
                this.template.exchange(url, HttpMethod.POST, request, TenantDtoResp.class);
        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(),
                "id", "createdDate", "modifiedDate", "name");
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createTenantTest_unsuccessFlow(
            Map<String, Object> dtos
    ) {
        final String url = "/tenants";
        request = new HttpEntity(dtos.get("null"), headers);
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);
        //Verify this exception because of validation null entity can't be accepted by controller
        assertEquals(415, response.getStatusCodeValue());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createTenantTestWithMissedRequiredField_unsuccessFlow(
            Map<String, Object> dtos
    ) {
        final String url = "/tenants";
        TenantDtoCreateReq requestDto = (TenantDtoCreateReq) dtos.get("request");
        requestDto.setDescription(null);
        request = new HttpEntity<>(requestDto, headers);

        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);
        //Verify this exception because of validation missed field
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Validation failed for object='tenantDtoCreateReq'. Error count: 1", response.getBody().getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void updateTenantTest_successFlow(
            Map<String, Object> dtos,
            Map<String, String> tenantIds
    ) {
        final String url = "/tenants/" + tenantIds.get("real-2");
        TenantDtoCreateReq requestDto = (TenantDtoCreateReq) dtos.get("request");
        requestDto.setDescription("New Company");
        request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<TenantDtoResp> response =
                this.template.exchange(url, HttpMethod.PUT, request, TenantDtoResp.class);
        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(), "createdDate", "modifiedDate", "name");
        assertThat(response.getBody()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void updateTenantTest_unsuccessFlow(
            Map<String, Object> dtos,
            Map<String, String> tenantIds
    ) {
        final String url = "/tenants/" + tenantIds.get("fake");
        TenantDtoCreateReq requestDto = (TenantDtoCreateReq) dtos.get("request");
        requestDto.setDescription("New Company");
        request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, ResponseMessageDto.class);
        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    void deleteTenant_successFlow() {
        final String url = "/tenants/test_tenant_3";
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, null, ResponseMessageDto.class);
        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteTenant_unsuccessFlow() {
        final String url = "/tenants/0";
        ResponseEntity<ResponseMessageDto> response
                = template.exchange(url, HttpMethod.DELETE, null, ResponseMessageDto.class);
        //Verify request not succeed
        checkEntityNotFoundException(response);
    }

    @Test
    void findAllTenantsTest() {
        final String url = "/tenants";
        ResponseEntity<List<TenantDtoResp>> response =
                template.exchange(url, HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<TenantDtoResp>>(){});
        //Checking if status code is correct
        assertEquals(200, response.getStatusCodeValue());
    }

    private void checkEntityNotFoundException(ResponseEntity<ResponseMessageDto> response) {
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    private static Stream<Arguments> provideTestData(){
        TenantDtoResp response = new TenantDtoResp();
        response.setId("test_tenant_1");
        response.setActive(true);
        response.setDescription("Company-1");

        Map<String, Object> dtos = new HashMap<>();
        dtos.put("request", getListOfTenants().get(0));
        dtos.put("null", null);
        dtos.put("response", response);
        Map<String, String> tenantIds = new HashMap();
        tenantIds.put("real-1", "test_tenant_1");
        tenantIds.put("real-2", "test_tenant_2");
        tenantIds.put("fake", "0");
        return Stream.of(
                Arguments.of(dtos, tenantIds)
        );
    }
}
