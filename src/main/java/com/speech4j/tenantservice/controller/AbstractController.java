package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.entity.AbstractEntity;
import com.speech4j.tenantservice.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class AbstractController<E extends AbstractEntity, S extends CommonService<E>>
        implements CommonController<E> {

    private final S service;

    @Autowired
    protected AbstractController(S service) {
        this.service = service;
    }

    @Override
    public E save(@RequestBody E entity) {
        return service.create(entity);
    }

    @Override
    public E findById(Long id) {
        return service.findById(id);
    }

    @Override
    public E update(E entity) {
        return service.update(entity);
    }

    @Override
    public void delete(Long id) {
        service.deleteById(id);
    }

    @Override
    public List<E> findAll() {
        return service.findAll();
    }
}
