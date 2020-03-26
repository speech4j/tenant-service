package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.validation.ExistData;
import com.speech4j.tenantservice.dto.validation.NewData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface EntityController<DtoRequest, DtoResponse> {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create entity",
            responses = {
                    @ApiResponse(responseCode = "400", description = "Validation exception")})
    DtoResponse save(
            @Parameter(description = "Entity object that needs to be added to db", required = true)
            @Validated({NewData.class}) @RequestBody DtoRequest dto
    );

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get entity by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Entity is found"),
                    @ApiResponse(responseCode = "404", description = "Entity not found")})
    DtoResponse findById(
            @Parameter(description = "Entity id for get", required = true)
            @PathVariable("id") String id
    );

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update entity by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Entity is updated"),
                    @ApiResponse(responseCode = "404", description = "Entity not found"),
                    @ApiResponse(responseCode = "400", description = "Validation exception")})
    DtoResponse update(
            @Parameter(description = "Entity object that needs to be updated", required = true)
            @Validated({ExistData.class}) @RequestBody DtoRequest dto,
            @Parameter(description = "Entity id for update", required = true)
            @PathVariable String id
    );


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete entity by ID",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Entity not found")})
    void delete(
            @Parameter(description = "Entity id for update", required = true)
            @PathVariable("id") String id
    );


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all entities by ID",
            description = "Get list of entities",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")})
    List<DtoResponse> findAll();
}