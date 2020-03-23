package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.ConfigDto;
import com.speech4j.tenantservice.entity.Config;
import com.speech4j.tenantservice.mapper.ConfigDtoMapper;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tenants/users/configs")
public class ConfigController implements EntityController<ConfigDto> {
    private EntityService<Config> service;
    private ConfigDtoMapper mapper;

    @Autowired
    public ConfigController(EntityService<Config> service, ConfigDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ConfigDto save(ConfigDto dto) {
        return mapper.toDto(service.create(mapper.toEntity(dto)));
    }

    @Override
    public ConfigDto findById(Long id) {
        return mapper.toDto(service.findById(id));
    }

    @Override
    public ConfigDto update(ConfigDto dto) {
        return mapper.toDto(service.update(mapper.toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        service.deleteById(id);
    }

    @Override
    public List<ConfigDto> findAll() {
        return mapper.toDtoList(service.findAll());
    }
}
