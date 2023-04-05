package com.database.integration.central.repositories;

import com.database.integration.central.repositories.custom_interface.CustomProductRepository;
import com.database.integration.core.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, CustomProductRepository {

    Optional<Product> findBySerialNumber(@Param("serialNumber") String serialNumber);
}
