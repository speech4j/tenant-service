package com.speech4j.tenantservice.mapper;

import com.speech4j.tenantservice.dto.UserDto;
import com.speech4j.tenantservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper extends AbstractEntityDtoMapper<User, UserDto> {
    @Override
    public User toEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .createdDate(dto.getCreatedDate())
                .updatedDate(dto.getUpdatedDate())
                .active(dto.isActive())
                .build();
    }

    @Override
    public UserDto toDto(User entity) {
        return UserDto.builder()
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
