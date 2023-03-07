package com.database.integration.central.repositories.impl;

import com.database.integration.central.repositories.custom_interface.CustomProductRepository;
import com.central.model.products.Product;

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
