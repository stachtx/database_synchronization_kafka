package com.database.integration.external.repositories;

import com.database.integration.core.model.products.ProductType;
import com.database.integration.external.repositories.custom_interface.CustomProductTypeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, UUID>, CustomProductTypeRepository {

}
