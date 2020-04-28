package org.speech4j.tenantservice.dto.request;

import org.speech4j.tenantservice.dto.validation.ExistData;
import org.speech4j.tenantservice.dto.validation.NewData;
import org.speech4j.tenantservice.entity.general.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
    @Pattern(groups = {NewData.class, ExistData.class}, regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$",
            message = "{password.validation}")
    private String password;
    private Role role;
}
