package com.database.integration.core.model;


import com.database.integration.core.utils.Identifiable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EnableAutoConfiguration
@Table(name = "DEPARTMENT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Department implements Serializable, Identifiable<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Version
    @Column(name = "VERSION")
    private long version;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Product> devices = new HashSet<>();

    @Override
    public String toString() {
        return String.format("id:" + id.toString() + " department name: " + name);
    }
}

