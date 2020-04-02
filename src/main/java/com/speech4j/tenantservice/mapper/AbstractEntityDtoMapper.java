package com.speech4j.tenantservice.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface AbstractEntityDtoMapper<DtoRequest, E, DtoResponse> {
    E toEntity(DtoRequest dto);

    DtoResponse toDto(E entity);

    default List<DtoResponse> toDtoList(List<E> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}