package com.speech4j.tenantservice.mapper;

import com.speech4j.tenantservice.dto.request.TenantDtoReq;
import com.speech4j.tenantservice.dto.response.TenantDtoResp;
import com.speech4j.tenantservice.entity.Tenant;
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
