package com.speech4j.tenantservice.mapper;

import com.speech4j.tenantservice.dto.request.TenantDtoReq;
import com.speech4j.tenantservice.dto.response.TenantDtoResp;
import com.speech4j.tenantservice.entity.Tenant;
import org.springframework.stereotype.Component;

@Component
public class TenantDtoMapper extends AbstractEntityDtoMapper<TenantDtoReq, Tenant, TenantDtoResp> {
    @Override
    public Tenant toEntity(TenantDtoReq dto) {
        return Tenant.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    public TenantDtoResp toDto(Tenant entity) {
        return TenantDtoResp.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdDate(entity.getCreatedDate())
                .active(entity.isActive())
                .build();
    }
}
