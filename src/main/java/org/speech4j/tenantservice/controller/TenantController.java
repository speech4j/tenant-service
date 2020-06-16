package org.speech4j.tenantservice.controller;

import org.speech4j.tenantservice.dto.request.TenantDtoCreateReq;
import org.speech4j.tenantservice.dto.request.TenantDtoUpdateReq;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;
import org.speech4j.tenantservice.entity.metadata.Tenant;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<TenantDtoResp> create(@Validated @RequestBody TenantDtoCreateReq dto) {
        return service.create(mapper.toEntity(dto));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<TenantDtoResp> findById(@PathVariable String id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<TenantDtoResp> updateTenant(@Validated @RequestBody TenantDtoUpdateReq dto,
                                            @PathVariable String id) {
        Tenant tenant = Tenant.builder().description(dto.getDescription()).build();
        return service.update(tenant, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteTenant(@PathVariable String id) {
        return service.deleteById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<TenantDtoResp> getAllTenants() {
       return service.getTenants();
    }


}
