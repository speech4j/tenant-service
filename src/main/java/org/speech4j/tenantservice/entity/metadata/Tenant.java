package org.speech4j.tenantservice.entity.metadata;


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

import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table("tenants")
public class Tenant{
    @Id
    private String  id;
    private String description;
    @CreatedDate
    @Column("createddate")
    private Timestamp createdDate;
    @LastModifiedDate
    @Column("modifieddate")
    private Timestamp modifiedDate;
    private boolean active;
}
