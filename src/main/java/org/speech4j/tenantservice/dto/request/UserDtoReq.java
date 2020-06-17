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
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Pattern( regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$",
            message = "must be at least 4 characters, no more than 8 characters, " +
                    "and must include at least one upper case letter," +
                    " one lower case letter, and one numeric digit")
    private String password;
    private Role role;
}
