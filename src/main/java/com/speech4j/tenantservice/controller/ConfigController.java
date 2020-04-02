package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.request.ConfigDtoReq;
import com.speech4j.tenantservice.dto.response.ConfigDtoResp;
import com.speech4j.tenantservice.dto.validation.NewData;
import com.speech4j.tenantservice.entity.Config;
import com.speech4j.tenantservice.entity.Tenant;
import com.speech4j.tenantservice.exception.EntityNotFoundException;
import com.speech4j.tenantservice.mapper.ConfigDtoMapper;
import com.speech4j.tenantservice.service.ConfigService;
import com.speech4j.tenantservice.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("tenants/{id}/configs")
public class ConfigController{
    private ConfigService configService;
    private TenantService tenantService;
    private ConfigDtoMapper mapper;

    @Autowired
    public ConfigController(ConfigService configService,
                            TenantService tenantService,
                            ConfigDtoMapper mapper) {
        this.configService = configService;
        this.tenantService = tenantService;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create user",
            responses = {
                    @ApiResponse(responseCode = "400", description = "Validation exception")})
    public ConfigDtoResp save(
            @Parameter(description = "Config object that needs to be added to db", required = true)
            @Validated({NewData.class}) @RequestBody ConfigDtoReq dto,
            @Parameter(description = "Tenant id for saving", required = true)
            @PathVariable String id
    ) {
        Tenant tenant = tenantService.findById(id);
        Config config = mapper.toEntity(dto);
        config.setTenant(tenant);
        return mapper.toDto(configService.create(config));
    }

    @GetMapping("/{configId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get com.speech4j.tenantservice.config by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Config is found"),
                    @ApiResponse(responseCode = "404", description = "Config not found")})
    public ConfigDtoResp findById(
            @Parameter(description = "Tenant id for get", required = true)
            @PathVariable String id,
            @Parameter(description = "Config id for get", required = true)
            @PathVariable String configId
    ) {
        checkIfExist(configId, id);
        return mapper.toDto(configService.findById(configId));
    }

    @PutMapping("/{configId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update entity by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Config is updated"),
                    @ApiResponse(responseCode = "404", description = "Config not found"),
                    @ApiResponse(responseCode = "400", description = "Validation exception")})
    public ConfigDtoResp update(
            @Parameter(description = "Config object that needs to be added to db", required = true)
            @Validated({NewData.class}) @RequestBody ConfigDtoReq dto,
            @Parameter(description = "Tenant id for update", required = true)
            @PathVariable String id,
            @Parameter(description = "Config id for update", required = true)
            @PathVariable String configId
    ) {
        checkIfExist(configId, id);
        return mapper.toDto(configService.update(mapper.toEntity(dto), configId));
    }

    @DeleteMapping("/{configId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete com.speech4j.tenantservice.config by ID",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Config not found")})
    public void delete(
            @Parameter(description = "Tenant id for delete", required = true)
            @PathVariable String id,
            @Parameter(description = "Config id for delete", required = true)
            @PathVariable String configId
    ) {
        checkIfExist(configId, id);
        configService.deleteById(configId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all configs by ID",
            description = "Get list of entities",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")})
    public List<ConfigDtoResp> findAll(
            @Parameter(description = "Tenant id for get", required = true)
            @PathVariable String id
    ) {
        return mapper.toDtoList(configService.findAllById(id));
    }

    private void checkIfExist(String configId, String tenantId){
        Config config = configService.findById(configId);
        if (!config.getTenant().getId().equals(tenantId))
            throw new EntityNotFoundException("Config not found!");
    }
}
