package com.database.integration.central.repositories.impl;

import com.database.integration.central.repositories.custom_interface.CustomDepartmentRepository;
import com.database.integration.core.model.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;


public class DepartmentRepositoryImpl implements CustomDepartmentRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void detach(Department entity) {
    entityManager.detach(entity);
  }

  @Override
  public void merge(Department entity) {
    Session session = (Session) entityManager.getDelegate();
    detach(entity);
    session.replicate(entity, ReplicationMode.LATEST_VERSION);
  }
}
