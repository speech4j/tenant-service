package com.speech4j.tenantservice.dto.request;

import com.speech4j.tenantservice.dto.validation.ExistData;
import com.speech4j.tenantservice.dto.validation.NewData;
import com.speech4j.tenantservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoReq {
    @NotBlank(groups = {NewData.class, ExistData.class}, message = "{field.not.empty}")
    private String firstName;
    @NotBlank(groups = {NewData.class, ExistData.class}, message = "{field.not.empty}")
    private String lastName;
    @NotBlank(groups = {NewData.class, ExistData.class}, message = "{field.not.empty}")
    @Email(groups = {NewData.class, ExistData.class}, message = "{email.not.valid}")
    private String email;
    @NotBlank(groups = {NewData.class, ExistData.class}, message = "{field.not.empty}")
    @Size(groups = {NewData.class, ExistData.class}, min = 6, message = "{password.validation.length}")
    @Pattern(groups = {NewData.class, ExistData.class}, regexp = ".*[A-Za-z]+.*",
            message = "{password.validation.letter}")
    @Pattern(groups = {NewData.class, ExistData.class}, regexp = ".*[0-9]+.*",
            message = "{password.validation.letter}")
    private String password;
    private Role role;
}
