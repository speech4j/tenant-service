package org.speech4j.tenantservice.entity.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.io.Serializable;


@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@javax.persistence.Table(name = "tenant_configs")
@org.springframework.data.relational.core.mapping.Table("tenant_configs")
public class Config implements Serializable {
    @javax.persistence.Id
    @org.springframework.data.annotation.Id
    private String id;
    @javax.persistence.Column(name = "apiname")
    @org.springframework.data.relational.core.mapping.Column("apiname")
    private ApiName apiName;
    @javax.persistence.Column(name = "tenantid")
    @org.springframework.data.relational.core.mapping.Column("tenantid")
    private String tenantId;
    private String credentials;
}