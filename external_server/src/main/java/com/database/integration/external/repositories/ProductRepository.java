package com.database.integration.external.repositories;

import com.database.integration.core.model.Product;
import com.database.integration.external.repositories.custom_interface.CustomProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, CustomProductRepository {

    Optional<Product> findBySerialNumber(@Param("serialNumber") String serialNumber);
}
