package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.UserDto;
import com.speech4j.tenantservice.entity.User;
import com.speech4j.tenantservice.mapper.UserDtoMapper;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tenants/users")
public class UserController implements EntityController<UserDto> {
    private EntityService<User> service;
    private UserDtoMapper mapper;

    @Autowired
    public UserController(EntityService<User> service, UserDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public UserDto save(UserDto dto) {
        return mapper.toDto(service.create(mapper.toEntity(dto)));
    }

    @Override
    public UserDto findById(Long id) {
        return mapper.toDto(service.findById(id));
    }

    @Override
    public UserDto update(UserDto dto) {
        return mapper.toDto(service.update(mapper.toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        service.deleteById(id);
    }

    @Override
    public List<UserDto> findAll() {
        return mapper.toDtoList(service.findAll());
    }
}
