package com.speech4j.tenantservice.service.impl;

import com.speech4j.tenantservice.entity.general.Role;
import com.speech4j.tenantservice.entity.general.User;
import com.speech4j.tenantservice.exception.UserNotFoundException;
import com.speech4j.tenantservice.repository.UserRepository;
import com.speech4j.tenantservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository repository;
    private PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository repository,
                           PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public User create(User entity) {
        //Encoding password before saving
        entity.setPassword(encoder.encode(entity.getPassword()));
        //Checking if role is missed
        if (entity.getRole() == null) {
            entity.setRole(Role.ADMIN);
        }
         return repository.save(entity);
    }

    @Override
    public User findById(String id) {
        return findByIdOrThrowException(id);
    }

    @Override
    public User update(User entity, String id) {
        User user = findByIdOrThrowException(id);
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        //Encoding password before updating
        user.setPassword(encoder.encode(entity.getPassword()));

        return repository.save(user);
    }

    @Override
    public void deleteById(String id) {
        User user = findByIdOrThrowException(id);
        user.setActive(false);
        repository.save(user);
    }

    @Override
    public List<User> findAllById(String id) {
        List<User> list = repository.findAllByTenantId(id).stream().filter(u->u.isActive()).collect(Collectors.toList());
        if (!list.isEmpty()){
            return list;
        }else {
            throw new UserNotFoundException("User not found!");
        }
    }

    private User findByIdOrThrowException(String id) {
        //Checking if user is found
        return repository.findById(id).filter(user-> user.isActive())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }
}
