package com.database.integration.external.repositories.impl;

import com.database.integration.core.model.products.Product;
import com.database.integration.external.repositories.custom_interface.CustomProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class ProductRepositoryImpl implements CustomProductRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void detach(Product entity) {
        entityManager.detach(entity);
    }
}
