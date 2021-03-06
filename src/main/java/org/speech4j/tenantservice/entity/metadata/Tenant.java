package org.speech4j.tenantservice.entity.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tenants")
@EntityListeners(AuditingEntityListener.class)
public class Tenant implements Serializable {
    @Id
    @Column(unique = true)
    private String  id;
    private String description;
    @Column(nullable = false, updatable = false, name = "createddate")
    @CreatedDate
    private Timestamp createdDate;
    @LastModifiedDate
    @Column(name = "modifieddate")
    private Timestamp modifiedDate;
    private boolean active;
}
