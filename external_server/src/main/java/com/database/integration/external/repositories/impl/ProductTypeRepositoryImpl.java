package com.database.integration.external.repositories.impl;

import com.database.integration.core.model.products.ProductType;
import com.database.integration.external.repositories.custom_interface.CustomProductTypeRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class ProductTypeRepositoryImpl implements CustomProductTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void detach(ProductType entity) {
        entityManager.detach(entity);
    }
}
