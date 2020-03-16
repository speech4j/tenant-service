package com.speech4j.tenantservice.service;

import com.speech4j.tenantservice.entity.AbstractEntity;
import com.speech4j.tenantservice.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractService<E extends AbstractEntity, R extends CommonRepository<E>>
        implements CommonService<E> {
    protected final R repository;

    @Autowired
    public AbstractService(R repository) {
        this.repository = repository;
    }

    @Override
    public E create(E entity) {
        return repository.save(entity);
    }

    @Override
    public E findById(Long id) {
        return findByIdOrThrowException(id);
    }

    @Override
    public E update(E entity) {
        findByIdOrThrowException(entity.getId());
        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        findByIdOrThrowException(id);
        repository.deleteById(id);

    }

    @Override
    public List<E> findAll() {
        return (List<E>) repository.findAll();
    }

    private E findByIdOrThrowException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entity not found!"));
    }

}
