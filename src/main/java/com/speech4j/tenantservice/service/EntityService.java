package com.speech4j.tenantservice.service;

import java.util.List;

public interface EntityService<E> {
    E create(E entity);

    E findById(String id);

    E update(E entity, String id);

    void deleteById(String id);

    List<E> findAllById(String id);

    List<E> findAll();
}