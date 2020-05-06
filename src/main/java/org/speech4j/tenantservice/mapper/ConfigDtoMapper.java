package org.speech4j.tenantservice.mapper;

import org.speech4j.tenantservice.dto.request.ConfigDtoReq;
import org.speech4j.tenantservice.dto.response.ConfigDtoResp;
import org.speech4j.tenantservice.entity.general.Config;
import org.springframework.stereotype.Component;

@Component
public class ConfigDtoMapper implements AbstractEntityDtoMapper<ConfigDtoReq, Config, ConfigDtoResp> {
    @Override
    public Config toEntity(ConfigDtoReq dto) {
        return Config.builder()
                .apiName(dto.getApiName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

    @Override
    public ConfigDtoResp toDto(Config entity) {
        return ConfigDtoResp.builder()
                .id(entity.getId())
                .apiName(entity.getApiName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .build();
    }
}