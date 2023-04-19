package com.database.integration.core.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
