package com.speech4j.tenantservice.mapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractEntityDtoMapper<DtoRequest, E, DtoResponse> {
    public abstract E toEntity(DtoRequest dto);

    public abstract DtoResponse toDto(E entity);

    public List<DtoResponse> toDtoList(List<E> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}