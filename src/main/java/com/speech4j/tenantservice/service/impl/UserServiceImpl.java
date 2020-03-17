package com.speech4j.tenantservice.service.impl;

import com.speech4j.tenantservice.entity.User;
import com.speech4j.tenantservice.repository.UserRepository;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements EntityService<User> {
    private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User entity) {
        return repository.save(entity);
    }

    @Override
    public User findById(Long id) {
        return findByIdOrThrowException(id);
    }

    @Override
    public User update(User entity) {
        return findByIdOrThrowException(entity.getId());
    }

    @Override
    public void deleteById(Long id) {
        findByIdOrThrowException(id);
        repository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    private User findByIdOrThrowException(Long id) {
        Optional<User> user = repository.findById(id);
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }
}
