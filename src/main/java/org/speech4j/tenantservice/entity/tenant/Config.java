package org.speech4j.tenantservice.entity.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONObject;
import org.speech4j.tenantservice.mapper.json.JSONObjectConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tenant_configs")
@ToString
public class Config implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String  id;
    @Enumerated(EnumType.STRING)
    @Column(name = "apiname")
    private ApiName apiName;
    @Column(name = "tenantid")
    private String tenantId;
    @NonNull
    @Column(columnDefinition = "TEXT")
    @Convert(converter=JSONObjectConverter.class)
    private JSONObject credentials;
}
