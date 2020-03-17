package com.speech4j.tenantservice.mapper;

import com.speech4j.tenantservice.dto.TenantDto;
import com.speech4j.tenantservice.entity.Tenant;

public class TenantDtoMapper extends AbstractEntityDtoMapper<Tenant, TenantDto> {
    @Override
    public Tenant toEntity(TenantDto dto) {
        return Tenant.builder()
                .id(dto.getId())
                .name(dto.getName())
                .createdDate(dto.getCreatedDate())
                .active(dto.isActive())
                .build();
    }

    @Override
    public TenantDto toDto(Tenant entity) {
        return TenantDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdDate(entity.getCreatedDate())
                .active(entity.isActive())
                .build();
    }
}
