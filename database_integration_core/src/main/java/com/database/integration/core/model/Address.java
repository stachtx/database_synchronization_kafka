package com.database.integration.core.model;


import com.database.integration.core.utils.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Getter
@Setter
@EnableAutoConfiguration
@Table(name = "ADDRESS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = null;

    @Version
    @Column(name = "VERSION")
    private long version;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Column(name = "STREET", nullable = false)
    private String street;

    @Column(name = "BUILDING_NUMBER", nullable = false)
    private String buildingNumber;

    @Column(name = "FLAT_NUMBER")
    private String flatNumber;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

}
