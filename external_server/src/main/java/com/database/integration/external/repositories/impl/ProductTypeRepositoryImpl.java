package com.database.integration.external.repositories.impl;

import com.database.integration.core.model.products.ProductType;
import com.database.integration.external.repositories.custom_interface.CustomProductTypeRepository;
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
  public void merge(ProductType productType) {
    Session session = (Session) entityManager.getDelegate();
    detach(productType);
    session.replicate(productType, ReplicationMode.LATEST_VERSION);
  }
}
