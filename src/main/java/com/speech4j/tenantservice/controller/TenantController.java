package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.TenantDto;
import com.speech4j.tenantservice.entity.Tenant;
import com.speech4j.tenantservice.mapper.TenantDtoMapper;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tenants")
public class TenantController implements EntityController<TenantDto> {
    private EntityService<Tenant> service;
    private TenantDtoMapper mapper;

    @Autowired
    public TenantController(EntityService<Tenant> service, TenantDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public TenantDto save(TenantDto dto) {
        return mapper.toDto(service.create(mapper.toEntity(dto)));
    }

    @Override
    public TenantDto findById(Long id) {
        return mapper.toDto(service.findById(id));
    }

    @Override
    public TenantDto update(TenantDto dto) {
        return mapper.toDto(service.update(mapper.toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        service.deleteById(id);
    }

    @Override
    public List<TenantDto> findAll() {
        return mapper.toDtoList(service.findAll());
    }
}
