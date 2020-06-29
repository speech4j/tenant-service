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
@javax.persistence.Table(name = "configs")
@org.springframework.data.relational.core.mapping.Table("configs")
public class Config implements Serializable {
    @javax.persistence.Id
    @org.springframework.data.annotation.Id
    private String id;
    private ApiName apiName;
    private String tenantId;
    private String credentials;
}
