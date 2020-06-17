package org.speech4j.tenantservice.entity.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("configs")
@ToString
public class Config implements Serializable {
    @Id
    private String id;
    @Column("apiname")
    private ApiName apiName;
    @Column("tenantid")
    private String tenantId;
    private String credentials;
}
