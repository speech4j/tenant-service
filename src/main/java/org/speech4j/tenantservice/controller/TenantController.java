package org.speech4j.tenantservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.speech4j.tenantservice.dto.request.TenantDtoReq;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;
import org.speech4j.tenantservice.mapper.TenantDtoMapper;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tenants")
public class TenantController{
    private TenantService service;
    private TenantDtoMapper mapper;

    @Autowired
    public TenantController(TenantService service, TenantDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create tenant",
            responses = {
                    @ApiResponse(responseCode = "400", description = "Validation exception")})
    public TenantDtoResp save(
            @Parameter(description = "Tenant object that needs to be added to db", required = true)
            @Validated @RequestBody TenantDtoReq dto) {
        return mapper.toDto(service.create(mapper.toEntity(dto)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get entity by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tenant is found"),
                    @ApiResponse(responseCode = "404", description = "Tenant not found")})
    public TenantDtoResp findById(
            @Parameter(description = "Tenant id for get", required = true)
            @PathVariable("id") String id) {
        return mapper.toDto(service.findById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update tenant by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tenant is updated"),
                    @ApiResponse(responseCode = "404", description = "Tenant not found"),
                    @ApiResponse(responseCode = "400", description = "Validation exception")})
    public TenantDtoResp update(
            @Parameter(description = "Tenant object that needs to be updated", required = true)
            @Validated @RequestBody TenantDtoReq dto,
            @Parameter(description = "Tenant id for update", required = true)
            @PathVariable String id) {
        return mapper.toDto(service.update(mapper.toEntity(dto), id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete tenant by ID",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Tenant not found")})
    public void delete(
            @Parameter(description = "Tenant id for delete", required = true)
            @PathVariable("id") String id) {
        service.deleteById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all tenants",
            description = "Get list of tenants",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")})
    public List<TenantDtoResp> findAll() {
        return mapper.toDtoList(service.findAll());
    }
}
