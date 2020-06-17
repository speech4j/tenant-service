package org.speech4j.tenantservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.dto.request.TenantDtoCreateReq;
import org.speech4j.tenantservice.dto.request.TenantDtoUpdateReq;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;
import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.speech4j.tenantservice.mapper.TenantDtoMapper;
import org.speech4j.tenantservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import javax.validation.Validation;
import javax.validation.Validator;

import static org.speech4j.tenantservice.util.MessageValidationUtil.validate;

@RestController
@Slf4j
@RequestMapping("/tenants")
public class TenantController {
    private TenantService service;
    private TenantDtoMapper mapper;
    private Validator validator;

    @Autowired
    public TenantController(TenantService service,
                            TenantDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TenantDtoResp> create(@RequestBody TenantDtoCreateReq dto) {
        return validate(dto)
                .flatMap(response-> service.create(mapper.toEntity(dto)));
    }

    @GetMapping("/{id}")
    public Mono<TenantDtoResp> findById(@PathVariable String id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Mono<TenantDtoResp> updateTenant(@RequestBody TenantDtoUpdateReq dto,
                                            @PathVariable String id) {
        return validate(dto)
                .flatMap(response->{
                    Tenant tenant = Tenant.builder().description(dto.getDescription()).build();
                    return service.update(tenant, id);
                });

    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTenant(@PathVariable String id) {
        return service.deleteById(id);
    }

    @GetMapping
    public Flux<TenantDtoResp> getAllTenants() {
        return service.getTenants();
    }


}
