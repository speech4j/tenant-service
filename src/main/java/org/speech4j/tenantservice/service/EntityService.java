package org.speech4j.tenantservice.service;

public interface EntityService<E> {
    E create(E entity);

    E findById(String id);

    E update(E entity, String id);

    void deleteById(String id);
}