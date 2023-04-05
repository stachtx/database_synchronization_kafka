package com.database.integration.external.repositories.impl;

import com.database.integration.core.model.Department;
import com.database.integration.external.repositories.custom_interface.CustomDepartmentRepository;
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
  public void merge(Department department) {
    Session session = (Session) entityManager.getDelegate();
    detach(department);
    session.replicate(department, ReplicationMode.LATEST_VERSION);
  }
}
