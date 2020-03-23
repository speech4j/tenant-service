package com.speech4j.tenantservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.speech4j.tenantservice.dto.validation.ExistData;
import com.speech4j.tenantservice.dto.validation.NewData;
import com.speech4j.tenantservice.entity.Role;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

import static com.speech4j.tenantservice.dto.validation.Message.REQUIRED_EMPTY;
import static com.speech4j.tenantservice.dto.validation.Message.REQUIRED_NOT_EMPTY;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    private String firstName;
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    private String lastName;
    @NotNull(groups = {NewData.class, ExistData.class}, message = REQUIRED_NOT_EMPTY)
    @Email(groups = {NewData.class, ExistData.class}, message = "Please, enter a valid email")
    private String email;
    @NotNull(groups = {NewData.class,ExistData.class}, message = REQUIRED_NOT_EMPTY)
    @Size(groups = {NewData.class, ExistData.class}, min = 6, message = "Password must be at least 6 symbols long")
    @Pattern(groups = {NewData.class, ExistData.class}, regexp = ".*[A-Za-z]+.*",
            message = "Pattern must contain at least 1 alphabetical character")
    @Pattern(groups = {NewData.class, ExistData.class}, regexp = ".*[0-9]+.*",
            message = "Pattern must contain at least 1 numeric character")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Null(groups = {NewData.class, ExistData.class}, message = REQUIRED_EMPTY)
    private Role role;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private boolean active;
}
