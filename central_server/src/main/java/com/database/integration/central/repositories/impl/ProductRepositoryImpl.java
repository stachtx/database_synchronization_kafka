package com.database.integration.central.repositories.impl;

import com.database.integration.central.repositories.custom_interface.CustomProductRepository;
import com.database.integration.core.model.products.Product;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
  public void merge(Product entity) {
    Session session = (Session) entityManager.getDelegate();
    detach(entity);
    session.replicate(entity, ReplicationMode.LATEST_VERSION);
  }

}
