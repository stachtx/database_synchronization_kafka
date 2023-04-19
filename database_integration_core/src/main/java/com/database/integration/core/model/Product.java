package com.database.integration.core.model;

import com.database.integration.core.utils.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Data
@Builder
@AllArgsConstructor
@EnableAutoConfiguration
@Table(name = "PRODUCT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product implements Serializable, Identifiable<UUID> {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id = null;

  @Version
  @Column(name = "VERSION")
  private long version;

  @Column(name = "SERIAL_NUMBER", nullable = false, unique = true)
  private String serialNumber;

  @Column(name = "STATUS", nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductStatus status;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  private ProductType productType;

  @Basic
  @NotNull
  @Column(name = "LAST_UPDATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Calendar lastUpdate;

  @Basic
  @NotNull
  @Column(name = "CREATE_DATE", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Calendar createDate;

  @Column(name = "DELETED", nullable = false)
  private boolean deleted;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  private Department department;

  public Product() {
  }

  public Product(UUID id, long version) {
    this.id = id;
    this.version = version;
  }

  @Override
  public String toString() {
    return MessageFormat.format("id:{0} serial number: {1}", id, serialNumber);
  }
}
