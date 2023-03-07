package com.centralserver.model.users;

import com.centralserver.utils.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@EnableAutoConfiguration
@Table(name = "USERDATA")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Userdata implements Serializable, Identifiable<UUID> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "com.centralserver.utils.FallbackUUIDGenerator", parameters = {
    @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
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
