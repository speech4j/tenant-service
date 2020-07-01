package org.speech4j.tenantservice.entity.metadata;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@javax.persistence.Table(name = "tenants")
@org.springframework.data.relational.core.mapping.Table("tenants")
public class Tenant implements Serializable {
    @Id
    @org.springframework.data.annotation.Id
    private String id;
    private String description;
    @javax.persistence.Column(name = "createddate")
    @org.springframework.data.relational.core.mapping.Column("createddate")
    private LocalDate createdDate;
    @javax.persistence.Column(name = "modifieddate")
    @org.springframework.data.relational.core.mapping.Column("modifieddate")
    private LocalDate modifiedDate;
    private boolean active;
}