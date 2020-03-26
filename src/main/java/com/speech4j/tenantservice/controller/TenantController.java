package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.request.TenantDtoReq;
import com.speech4j.tenantservice.dto.response.TenantDtoResp;
import com.speech4j.tenantservice.entity.Tenant;
import com.speech4j.tenantservice.mapper.TenantDtoMapper;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tenants")
public class TenantController implements EntityController<TenantDtoReq, TenantDtoResp> {
    private EntityService<Tenant> service;
    private TenantDtoMapper mapper;

    @Autowired
    public TenantController(EntityService<Tenant> service, TenantDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public TenantDtoResp save(TenantDtoReq dto) {
        return mapper.toDto(service.create(mapper.toEntity(dto)));
    }

    @Override
    public TenantDtoResp findById(Long id) {
        return mapper.toDto(service.findById(id));
    }

    @Override
    public TenantDtoResp update(TenantDtoReq dto, Long id) {
        return mapper.toDto(service.update(mapper.toEntity(dto), id));
    }

    @Override
    public void delete(Long id) {
        service.deleteById(id);
    }

    @Override
    public List<TenantDtoResp> findAll() {
        return mapper.toDtoList(service.findAll());
    }
}
