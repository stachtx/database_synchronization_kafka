package com.database.integration.central.repositories.impl;

import com.database.integration.central.repositories.custom_interface.CustomProductTypeRepository;
import com.central.model.products.ProductType;

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
