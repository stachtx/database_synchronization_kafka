package com.database.integration.core.model.products;


import com.database.integration.core.utils.Identifiable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@EnableAutoConfiguration
@Table(name = "PRODUCT_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductType implements Serializable, Identifiable<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = null;

    @Version
    @Column(name = "VERSION")
    private long version;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "MANUFACTURE", nullable = false)
    private String manufacture;

    @Column(name = "COST", nullable = false)
    private long cost;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productType", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    @Override
    public String toString() {
        return MessageFormat.format("id:{0} name: {1}", id, name);
    }
}
