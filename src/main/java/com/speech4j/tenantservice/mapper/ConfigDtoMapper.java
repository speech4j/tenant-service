package com.speech4j.tenantservice.mapper;

import com.speech4j.tenantservice.dto.ConfigDto;
import com.speech4j.tenantservice.entity.Config;

public class ConfigDtoMapper extends AbstractEntityDtoMapper<Config, ConfigDto> {
    @Override
    public Config toEntity(ConfigDto dto) {
        return Config.builder()
                .id(dto.getId())
                .apiName(dto.getApiName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

    @Override
    public ConfigDto toDto(Config entity) {
        return ConfigDto.builder()
                .id(entity.getId())
                .apiName(entity.getApiName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .build();
    }
}
