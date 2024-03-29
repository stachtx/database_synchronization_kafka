package com.database.integration.core.model;


import com.database.integration.core.utils.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_")
@EnableAutoConfiguration
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements UserDetails, Serializable, Identifiable<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = null;

    @Version()
    @Column(name = "VERSION")
    private long version;

    @Column(name = "USER_NAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ACCOUNT_EXPIRED", nullable = false)
    private boolean accountExpired;

    @Column(name = "ACCOUNT_LOCKED", nullable = false)
    private boolean accountLocked;

    @Column(name = "CREDENTIALS_EXPIRED", nullable = false)
    private boolean credentialsExpired;

    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USERDATA_ID")
    private Userdata userdata;

    @OrderBy
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_ROLES", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "USER_ROLE_ID", referencedColumnName = "ID"))
    private Set<UserRole> userRoles;

    @Override
    @JsonIgnore
    public Set<Authority> getAuthorities() {
        return this.getUserRoles().stream().flatMap(x -> x.getAuthorities().stream()).collect(
            Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired();
    }

    @Override
    public String toString() {
        return String.format("id:" + id.toString() + " username: " + username);
    }
}
