package com.centralserver.repositories.impl;

import com.centralserver.model.products.Product;
import com.centralserver.repositories.custom_interface.CustomProductRepository;

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
