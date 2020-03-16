package com.speech4j.tenantservice.controller;

import com.speech4j.tenantservice.entity.AbstractEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface CommonController<E extends AbstractEntity> {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    E save(@RequestBody E entity);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    E findById(@PathVariable("id") Long id);

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    E update(@RequestBody E entity);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("id") Long id);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<E> findAll();
}
