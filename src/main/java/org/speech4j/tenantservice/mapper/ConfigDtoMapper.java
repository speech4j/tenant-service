package org.speech4j.tenantservice.mapper;

import org.json.JSONObject;
import org.speech4j.tenantservice.dto.request.ConfigDtoReq;
import org.speech4j.tenantservice.dto.response.ConfigDtoResp;
import org.speech4j.tenantservice.entity.tenant.Config;
import org.springframework.stereotype.Component;

@Component
public class ConfigDtoMapper implements AbstractEntityDtoMapper<ConfigDtoReq, Config, ConfigDtoResp> {
    @Override
    public Config toEntity(ConfigDtoReq dto) {
        return Config.builder()
                .apiName(dto.getApiName())
                .credentials(new JSONObject(dto.getCredentials()))
                .build();
    }

    @Override
    public ConfigDtoResp toDto(Config entity) {
        return ConfigDtoResp.builder()
                .id(entity.getId())
                .apiName(entity.getApiName())
                .credentials(entity.getCredentials().toMap())
                .build();
    }
}
