package com.speech4j.tenantservice.mapper;

import com.speech4j.tenantservice.dto.request.UserDtoReq;
import com.speech4j.tenantservice.dto.response.UserDtoResp;
import com.speech4j.tenantservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper implements AbstractEntityDtoMapper<UserDtoReq,User, UserDtoResp> {
    @Override
    public User toEntity(UserDtoReq dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
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
                .updatedDate(entity.getUpdatedDate())
                .active(entity.isActive())
                .build();
    }
}
