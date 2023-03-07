package com.database.integration.central.repositories.impl;

import com.database.integration.central.repositories.custom_interface.CustomProductTypeRepository;
import com.database.integration.core.model.products.ProductType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;


public class ProductTypeRepositoryImpl implements CustomProductTypeRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void detach(ProductType entity) {
    entityManager.detach(entity);
  }

  @Override
  public void merge(ProductType entity) {
    Session session = (Session) entityManager.getDelegate();
    detach(entity);
    session.replicate(entity, ReplicationMode.LATEST_VERSION);
  }
}
