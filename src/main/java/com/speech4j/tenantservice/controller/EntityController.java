package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.dto.TenantDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface EntityController<D> {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    D save(@RequestBody D dto);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    D findById(@PathVariable("id") Long id);

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    D update(@RequestBody D dto);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("id") Long id);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<D> findAll();
}