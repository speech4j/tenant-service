package org.speech4j.tenantservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.speech4j.tenantservice.entity.tenant.Role;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDtoResp {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private LocalDate createdDate;
    private LocalDate modifiedDate;
    private boolean active;
}
