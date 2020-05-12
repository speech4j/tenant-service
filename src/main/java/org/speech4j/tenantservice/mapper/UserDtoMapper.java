package org.speech4j.tenantservice.mapper;

import org.speech4j.tenantservice.dto.request.UserDtoReq;
import org.speech4j.tenantservice.dto.response.UserDtoResp;
import org.speech4j.tenantservice.entity.tenant.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper implements AbstractEntityDtoMapper<UserDtoReq, User, UserDtoResp> {
    @Override
    public User toEntity(UserDtoReq dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .active(true)
                .build();
    }

    @Override
    public UserDtoResp toDto(User entity) {
        return UserDtoResp.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .active(entity.isActive())
                .build();
    }
}
