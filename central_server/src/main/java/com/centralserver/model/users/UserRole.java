package com.centralserver.model.users;

import com.centralserver.utils.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter
@EnableAutoConfiguration
@Table(name = "USER_ROLE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserRole implements Serializable, Identifiable<UUID> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "com.centralserver.utils.FallbackUUIDGenerator", parameters = {
    @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = null;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active;

    @OrderBy
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_ROLES_AUTHORITIES", joinColumns = @JoinColumn(name = "USER_ROLE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID"))
    private Collection<Authority> authorities;
}
