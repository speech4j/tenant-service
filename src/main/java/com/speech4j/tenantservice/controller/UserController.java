package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.entity.User;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController implements EntityController<User> {
    private EntityService<User> service;

    @Autowired
    public UserController(EntityService<User> service) {
        this.service = service;
    }

    @Override
    public User save(User entity) {
        return service.create(entity);
    }

    @Override
    public User findById(Long id) {
        return service.findById(id);
    }

    @Override
    public User update(User entity) {
        return service.update(entity);
    }

    @Override
    public void delete(Long id) {
        service.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return service.findAll();
    }
}
