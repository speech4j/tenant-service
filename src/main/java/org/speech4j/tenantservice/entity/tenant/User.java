package org.speech4j.tenantservice.entity.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;

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
    @javax.persistence.Column(name = "firstname")
    @org.springframework.data.relational.core.mapping.Column("firstname")
    private String firstName;
    @javax.persistence.Column(name = "lastname")
    @org.springframework.data.relational.core.mapping.Column("lastname")
    private String lastName;
    @javax.persistence.Column(unique = true)
    private String email;
    private String password;
    private Role role;
    @javax.persistence.Column(name = "createddate")
    @org.springframework.data.relational.core.mapping.Column("createddate")
    private LocalDate createdDate;
    @javax.persistence.Column(name = "modifieddate")
    @org.springframework.data.relational.core.mapping.Column("modifieddate")
    private LocalDate modifiedDate;
    private boolean active;
    @javax.persistence.Column(name = "tenantid")
    @org.springframework.data.relational.core.mapping.Column("tenantid")
    private String tenantId;
}
