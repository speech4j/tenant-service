package com.speech4j.tenantservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tenants")
public class Tenant  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private ZonedDateTime createdDate;
    private boolean active;
    @OneToMany(mappedBy = "tenant")
    private Set<User> users;
    @OneToMany(mappedBy = "tenant")
    private Set<Config> configs;


    public Tenant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(Set<Config> configs) {
        this.configs = configs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tenant tenant = (Tenant) o;
        return active == tenant.active &&
                Objects.equals(id, tenant.id) &&
                Objects.equals(name, tenant.name) &&
                Objects.equals(createdDate, tenant.createdDate) &&
                Objects.equals(users, tenant.users) &&
                Objects.equals(configs, tenant.configs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdDate, active, users, configs);
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdDate=" + createdDate +
                ", active=" + active +
                ", users=" + users +
                ", configs=" + configs +
                '}';
    }
}
