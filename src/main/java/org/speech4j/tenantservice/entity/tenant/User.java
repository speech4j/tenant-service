package org.speech4j.tenantservice.entity.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@javax.persistence.Table(name = "users")
@org.springframework.data.relational.core.mapping.Table("users")
public class User implements Serializable {
    @javax.persistence.Id
    @org.springframework.data.annotation.Id
    private String id;
    private String firstName;
    private String lastName;
    @javax.persistence.Column(unique = true)
    private String email;
    private String password;
    private Role role;
    private LocalDate createdDate;
    private LocalDate modifiedDate;
    private boolean active;
    private String tenantId;
}
