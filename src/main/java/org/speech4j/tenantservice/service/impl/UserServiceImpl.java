package org.speech4j.tenantservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.speech4j.tenantservice.dto.response.UserDtoResp;
import org.speech4j.tenantservice.entity.tenant.Role;
import org.speech4j.tenantservice.entity.tenant.User;
import org.speech4j.tenantservice.exception.DuplicateEntityException;
import org.speech4j.tenantservice.exception.SqlOperationException;
import org.speech4j.tenantservice.exception.UserNotFoundException;
import org.speech4j.tenantservice.mapper.UserDtoMapper;
import org.speech4j.tenantservice.repository.tenant.UserRepository;
import org.speech4j.tenantservice.service.TenantService;
import org.speech4j.tenantservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository repository;
    private TenantService tenantService;
    private PasswordEncoder encoder;
    private UserDtoMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository repository,
                           TenantService tenantService,
                           PasswordEncoder encoder,
                           UserDtoMapper mapper) {
        this.repository = repository;
        this.tenantService = tenantService;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @Override
    public Mono<UserDtoResp> create(User user, String... ids) {
        return tenantService.getById(ids[0])
                .flatMap(existingTenant -> {
                    //Encoding password before saving
                    user.setPassword(encoder.encode(user.getPassword()));
                    //Checking if role is missed
                    if (isNull(user.getRole())) {
                        user.setRole(Role.ADMIN);
                    }

                    return repository.save(user)
                            .onErrorResume(err -> {
                                if (err instanceof DataIntegrityViolationException) {
                                    log.error("USER-SERVICE: User with a specified email: [{}] already exists!", user.getEmail());
                                    return Mono.error(new DuplicateEntityException("User with a specified email already exists!"));
                                } else {
                                    log.error("USER-SERVICE: User update failed {}", err.getLocalizedMessage());
                                    return Mono.error(new SqlOperationException("User update failed with email: " + user.getEmail()));
                                }
                            })
                            .flatMap(createdUser -> {
                                log.debug("USER-SERVICE: User with [ id: {}] was successfully created!", createdUser.getId());
                                return Mono.just(user).map(mapper::toDto);
                            });
                });
    }

    @Override
    public Mono<UserDtoResp> getById(String... ids) {
        return checkIfExistUserWithSpecifiedTenantId(ids[0], ids[1]).map(mapper::toDto);
    }

    @Override
    public Mono<UserDtoResp> update(User user, String... ids) {
        return checkIfExistUserWithSpecifiedTenantId(ids[0], ids[1])
                .flatMap(existingUser -> {
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    //Encoding password before updating
                    existingUser.setPassword(encoder.encode(user.getPassword()));

                    return repository.save(existingUser).map(mapper::toDto)
                            .doOnSuccess(updatedConfig ->
                                    log.debug(
                                            "USER-SERVICE: User with [ id: {}] was successfully updated!",
                                            ids[1]
                                    ));
                });
    }

    @Override
    public Mono<Void> deleteById(String... ids) {
        return checkIfExistUserWithSpecifiedTenantId(ids[0], ids[1])
                .flatMap(existingUser ->
                        repository.deactivate(ids[1]).doOnSuccess(success ->
                                log.debug("USER-SERVICE: User with [ id: {}] was successfully deactivated!", ids[1]))
                );
    }

    @Override
    public Flux<UserDtoResp> getAllById(String tenantId) {
        return repository.getAllByTenantId(tenantId)
                .switchIfEmpty(
                        Mono.error(new UserNotFoundException("Users by the tenant id: [" + tenantId + "] were not found!"))
                )
                .onErrorResume(err -> {
                    log.error("USER-SERVICE: User the tenant id: [{}] were  not found!", tenantId);
                    return Mono.error(err);
                })
                .doOnNext(existingUser ->
                        log.debug("CONFIG-SERVICE: Users the tenant id: [{}] were successfully found!", tenantId))
                .filter(User::isActive)
                .map(mapper::toDto);
    }

    private Mono<User> checkIfExistUserWithSpecifiedTenantId(String tenantId, String userId) {
        return repository.findById(userId)
                .switchIfEmpty(
                        Mono.error(new UserNotFoundException("User by id: [" + userId + "] not found!"))
                )
                .onErrorResume(err -> {
                    log.error("USER-SERVICE: User by id: [{}] not found!", userId);
                    return Mono.error(err);
                })
                .flatMap(existingUser -> {
                    if (!existingUser.getTenantId().equals(tenantId)) {
                        log.error("USER-SERVICE: User by id: [{}] not found!", userId);
                        return Mono.error(new UserNotFoundException("User with a specified id: [" + userId + "] not found"));
                    }
                    log.debug("USER-SERVICE: User with [ tenant id: {}] successfully found!", tenantId);
                    return Mono.just(existingUser);
                });
    }
}
