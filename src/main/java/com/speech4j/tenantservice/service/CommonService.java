package com.speech4j.tenantservice.service;

import com.speech4j.tenantservice.entity.AbstractEntity;

import java.util.List;

public interface CommonService<E extends AbstractEntity> {
    E create(E entity);

    E findById(Long id);

    E update(E entity);

    void deleteById(Long id);

    List<E> findAll();
}
