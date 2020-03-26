package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.validation.ExistData;
import com.speech4j.tenantservice.dto.validation.NewData;
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
    DtoResponse save(@Validated({NewData.class}) @RequestBody DtoRequest dto);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DtoResponse findById(@PathVariable("id") Long id);

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DtoResponse update(@Validated({ExistData.class}) @RequestBody DtoRequest dto, @PathVariable Long id);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("id") Long id);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<DtoResponse> findAll();
}