package org.speech4j.tenantservice.mapper;

import org.speech4j.tenantservice.dto.request.TenantDtoReq;
import org.speech4j.tenantservice.dto.response.TenantDtoResp;
import org.speech4j.tenantservice.entity.metadata.Tenant;
import org.springframework.stereotype.Component;

@Component
public class TenantDtoMapper implements AbstractEntityDtoMapper<TenantDtoReq, Tenant, TenantDtoResp> {
    @Override
    public Tenant toEntity(TenantDtoReq dto) {
        return Tenant.builder()
                .name(dto.getName())
                .active(true)
                .build();
    }

    @Override
    public TenantDtoResp toDto(Tenant entity) {
        return TenantDtoResp.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .active(entity.isActive())
                .build();
    }
}