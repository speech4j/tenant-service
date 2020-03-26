package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.request.UserDtoReq;
import com.speech4j.tenantservice.dto.response.UserDtoResp;
import com.speech4j.tenantservice.entity.User;
import com.speech4j.tenantservice.mapper.UserDtoMapper;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tenants/users")
public class UserController implements EntityController<UserDtoReq, UserDtoResp> {
    private EntityService<User> service;
    private UserDtoMapper mapper;

    @Autowired
    public UserController(EntityService<User> service, UserDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public UserDtoResp save(UserDtoReq dto) {
        return mapper.toDto(service.create(mapper.toEntity(dto)));
    }

    @Override
    public UserDtoResp findById(Long id) {
        return mapper.toDto(service.findById(id));
    }

    @Override
    public UserDtoResp update(UserDtoReq dto, Long id) {
        return mapper.toDto(service.update(mapper.toEntity(dto), id));
    }

    @Override
    public void delete(Long id) {
        service.deleteById(id);
    }

    @Override
    public List<UserDtoResp> findAll() {
        return mapper.toDtoList(service.findAll());
    }
}
