package com.database.integration.external.repositories.impl;

import com.database.integration.core.model.Product;
import com.database.integration.external.repositories.custom_interface.CustomProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;


public class ProductRepositoryImpl implements CustomProductRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void detach(Product entity) {
    entityManager.detach(entity);
  }

  @Override
  public void merge(Product product) {
    Session session = (Session) entityManager.getDelegate();
    detach(product);
    session.replicate(product, ReplicationMode.LATEST_VERSION);
  }
}
