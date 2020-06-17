package org.speech4j.tenantservice.entity.metadata;


import liquibase.sqlgenerator.core.AddAutoIncrementGeneratorDB2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table("tenants")
public class Tenant implements Serializable {
    @Id
    private String id;
    private String description;
    @Column("createddate")
    private Timestamp createdDate;
    @Column("modifieddate")
    private Timestamp modifiedDate;
    private boolean active;
}
