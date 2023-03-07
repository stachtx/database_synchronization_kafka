package com.database.integration.central.repositories;

import com.database.integration.central.repositories.custom_interface.CustomProductTypeRepository;

import com.database.integration.core.model.products.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, UUID>,
    CustomProductTypeRepository {

}
