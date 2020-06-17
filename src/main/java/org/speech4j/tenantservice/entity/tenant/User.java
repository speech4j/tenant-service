package org.speech4j.tenantservice.entity.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table("users")
public class User implements Serializable {
    @Id
    private String id;
    @Column("firstname")
    private String firstName;
    @Column("lastname")
    private String lastName;
    //@Column(unique = true)
    private String email;
    private String password;
    private Role role;
    @Column("createddate")
    @CreatedDate
    private LocalDate createdDate;
    @LastModifiedDate
    @Column("modifieddate")
    private LocalDate modifiedDate;
    private boolean active;
    @Column("tenantid")
    private String tenantId;
}
