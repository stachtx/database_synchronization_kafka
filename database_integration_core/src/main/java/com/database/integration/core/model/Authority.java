package com.database.integration.core.model;

import com.database.integration.core.utils.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@EnableAutoConfiguration
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "AUTHORITY", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
public class Authority implements GrantedAuthority, Serializable, Identifiable<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = null;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active;

    @Override
    public String getAuthority() {
        return getName();
    }
}
