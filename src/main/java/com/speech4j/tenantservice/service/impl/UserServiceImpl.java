package com.speech4j.tenantservice.service.impl;

import com.speech4j.tenantservice.entity.User;
import com.speech4j.tenantservice.exception.UserNotFoundException;
import com.speech4j.tenantservice.repository.UserRepository;
import com.speech4j.tenantservice.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements EntityService<User> {
    private UserRepository repository;
    private PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public User create(User entity) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.systemDefault());
        entity.setCreatedDate(currentTime);
        entity.setUpdatedDate(currentTime);
        entity.setPassword(encoder.encode(entity.getPassword()));
        return repository.save(entity);
    }

    @Override
    public User findById(Long id) {
        return findByIdOrThrowException(id);
    }

    @Override
    public User update(User entity) {
        User user = findByIdOrThrowException(entity.getId());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setPassword(encoder.encode(entity.getPassword()));
        user.setUpdatedDate(ZonedDateTime.now(ZoneId.systemDefault()));

        return repository.save(user);
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
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }
}
