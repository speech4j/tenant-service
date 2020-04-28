package org.speech4j.tenantservice.controller;

import org.speech4j.tenantservice.dto.request.UserDtoReq;
import org.speech4j.tenantservice.dto.response.UserDtoResp;
import org.speech4j.tenantservice.dto.validation.ExistData;
import org.speech4j.tenantservice.dto.validation.NewData;
import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.speech4j.tenantservice.entity.general.User;
import org.speech4j.tenantservice.exception.UserNotFoundException;
import org.speech4j.tenantservice.mapper.UserDtoMapper;
import org.speech4j.tenantservice.service.TenantService;
import org.speech4j.tenantservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tenants/{id}/users")
public class UserController{
    private UserService userService;
    private TenantService tenantService;
    private UserDtoMapper mapper;

    @Autowired
    public UserController(UserService userService,
                          TenantService tenantService,
                          UserDtoMapper mapper
    ) {
        this.userService = userService;
        this.tenantService = tenantService;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create user",
            responses = {
                    @ApiResponse(responseCode = "400", description = "Validation exception")})
    public UserDtoResp save(
            @Parameter(description = "User object that needs to be added to db", required = true)
            @Validated({NewData.class}) @RequestBody UserDtoReq dto,
            @Parameter(description = "Tenant id for saving", required = true)
            @PathVariable String id
    ) {
        Tenant tenant = tenantService.findById(id);
        User user = mapper.toEntity(dto);
        user.setTenant(tenant);
        return mapper.toDto(userService.create(user));
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User is found"),
                    @ApiResponse(responseCode = "404", description = "User not found")})
    public UserDtoResp findById(
            @Parameter(description = "Tenant id for get", required = true)
            @PathVariable String id,
            @Parameter(description = "User id for get", required = true)
            @PathVariable String userId
    ) {
        checkIfExist(userId,id);
        return mapper.toDto(userService.findById(userId));
    }


    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User is updated"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "400", description = "Validation exception")})
    public UserDtoResp update(
            @Parameter(description = "User object that needs to be updated", required = true)
            @Validated({ExistData.class}) @RequestBody UserDtoReq dto,
            @Parameter(description = "Tenant id for update", required = true)
            @PathVariable String id,
            @Parameter(description = "User userId for update", required = true)
            @PathVariable String userId
    ) {
        checkIfExist(userId,id);
        return mapper.toDto(userService.update(mapper.toEntity(dto), userId));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete user by ID",
            responses = {
                    @ApiResponse(responseCode = "404", description = "user not found")})
    public void delete(
            @Parameter(description = "Tenant id for delete", required = true)
            @PathVariable String id,
            @Parameter(description = "User id for delete", required = true)
            @PathVariable String userId
    ) {
        checkIfExist(userId,id);
        userService.deleteById(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all users by user ID",
            description = "Get list of users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")})
    public List<UserDtoResp> findAll(
            @Parameter(description = "Tenant id for get", required = true)
            @PathVariable String id
    ) {
         return mapper.toDtoList(userService.findAllById(id));
    }

    private void checkIfExist(String userId, String tenantId){
        User user = userService.findById(userId);
        if (!user.getTenant().getId().equals(tenantId))
            throw new UserNotFoundException("User not found!");
    }
}
