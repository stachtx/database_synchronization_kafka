package com.database.integration.core.model;

import com.database.integration.core.utils.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EnableAutoConfiguration
@Table(name = "USERDATA")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Userdata implements Serializable, Identifiable<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = null;

    @Version
    @Column(name = "VERSION")
    private long version;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SURNAME", nullable = false)
    private String surname;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "WORKPLACE")
    private String workplace;

    @Basic
    @NotNull
    @Column(name = "JOIN_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Calendar dateOfJoin;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;
}
