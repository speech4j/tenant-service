package com.speech4j.tenantservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "configs")
public class Config{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String apiName;
    private String username;
    private String password;
    @ManyToOne(targetEntity = Tenant.class, fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    private Tenant tenant;

    public Config() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(apiName, config.apiName) &&
                Objects.equals(username, config.username) &&
                Objects.equals(password, config.password) &&
                Objects.equals(tenant, config.tenant);
    }

    @Override
    public String toString() {
        return "Config{" +
                "apiName='" + apiName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", tenant=" + tenant +
                '}';
    }
}
