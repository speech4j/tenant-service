package org.speech4j.tenantservice.controller;

import org.speech4j.tenantservice.dto.request.ConfigDtoReq;
import org.speech4j.tenantservice.dto.response.ConfigDtoResp;
import org.speech4j.tenantservice.mapper.ConfigDtoMapper;
import org.speech4j.tenantservice.service.ConfigService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("tenants/{tenantId}/configs")
public class ConfigController {
    private ConfigService configService;
    private ConfigDtoMapper mapper;

    @Autowired
    public ConfigController(ConfigService configService,
                            ConfigDtoMapper mapper) {
        this.configService = configService;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ConfigDtoResp> create(
            @Validated @RequestBody ConfigDtoReq dto,
            @PathVariable String tenantId
    ) {
        return configService.create(mapper.toEntity(dto), tenantId);
    }

    @GetMapping("/{configId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ConfigDtoResp> getById(
            @PathVariable String tenantId,
            @PathVariable String configId
    ) {
        return configService.getById(tenantId, configId);
    }


    @PutMapping("/{configId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ConfigDtoResp> update(
            @Validated @RequestBody ConfigDtoReq dto,
            @PathVariable String tenantId,
            @PathVariable String configId
    ) {
        return configService.update(mapper.toEntity(dto), tenantId, configId);
    }

    @DeleteMapping("/{configId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String tenantId,
            @PathVariable String configId
    ) {
        configService.deleteById(tenantId, configId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<ConfigDtoResp> getAll(
            @PathVariable String tenantId
    ) {
        return configService.getAllById(tenantId);
    }
}
