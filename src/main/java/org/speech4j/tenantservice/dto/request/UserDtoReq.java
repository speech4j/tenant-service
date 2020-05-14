package org.speech4j.tenantservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.speech4j.tenantservice.entity.tenant.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDtoReq {
    @NotBlank( message = "{field.not.empty}")
    private String firstName;
    @NotBlank(message = "{field.not.empty}")
    private String lastName;
    @NotBlank(message = "{field.not.empty}")
    @Email( message = "{email.not.valid}")
    private String email;
    @NotBlank(message = "{field.not.empty}")
    @Pattern( regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$",
            message = "{password.validation}")
    private String password;
    private Role role;
}
