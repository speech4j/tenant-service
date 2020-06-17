package org.speech4j.tenantservice.controller;

import org.speech4j.tenantservice.dto.request.UserDtoReq;
import org.speech4j.tenantservice.dto.response.UserDtoResp;
import org.speech4j.tenantservice.mapper.UserDtoMapper;
import org.speech4j.tenantservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.speech4j.tenantservice.util.MessageValidationUtil.validate;

@RestController
@RequestMapping("tenants/{tenantId}/users")
public class UserController {
    private UserService userService;
    private UserDtoMapper mapper;

    @Autowired
    public UserController(UserService userService,
                          UserDtoMapper mapper
    ) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDtoResp> create(
            @RequestBody UserDtoReq dto,
            @PathVariable String tenantId
    ) {

        return validate(dto)
                .flatMap(response -> userService.create(mapper.toEntity(dto), tenantId));
    }

    @GetMapping("/{userId}")
    public Mono<UserDtoResp> findById(
            @PathVariable String tenantId,
            @PathVariable String userId
    ) {
        return userService.getById(tenantId, userId);
    }


    @PutMapping("/{userId}")
    public Mono<UserDtoResp> update(
            @RequestBody UserDtoReq dto,
            @PathVariable String tenantId,
            @PathVariable String userId
    ) {
        return validate(dto)
                .flatMap(response -> userService.update(mapper.toEntity(dto), tenantId, userId));
    }

    @DeleteMapping("/{userId}")
    public Mono<Void> delete(
            @PathVariable String tenantId,
            @PathVariable String userId
    ) {
        return userService.deleteById(tenantId, userId);
    }

    @GetMapping
    public Flux<UserDtoResp> findAll(
            @PathVariable String tenantId
    ) {
        return userService.getAllById(tenantId);
    }
}
