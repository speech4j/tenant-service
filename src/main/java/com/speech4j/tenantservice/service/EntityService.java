package com.speech4j.tenantservice.service;

import java.util.List;

public interface EntityService<E> {
    E create(E entity);

    E findById(Long id);

    E update(E entity, Long id);

    void deleteById(Long id);

    List<E> findAll();
}