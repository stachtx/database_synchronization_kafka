package com.centralserver.repositories.impl;

import com.centralserver.model.products.ProductType;
import com.centralserver.repositories.custom_interface.CustomProductTypeRepository;

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
