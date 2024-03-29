package com.database.integration.central.repositories.impl;

import com.database.integration.central.repositories.custom_interface.CustomProductRepository;
import com.database.integration.core.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;


public class ProductRepositoryImpl implements CustomProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void detach(Product entity) {
        entityManager.detach(entity);
    }

    @Override
    public void merge(Product entity) {
        Session session = (Session) entityManager.getDelegate();
        detach(entity);
        session.merge(entity);
    }

}
