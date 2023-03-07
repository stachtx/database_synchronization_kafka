package com.database.integration.external.repositories.impl;

import com.database.integration.core.model.Department;
import com.database.integration.external.repositories.custom_interface.CustomDepartmentRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class DepartmentRepositoryImpl implements CustomDepartmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void detach(Department entity) {
        entityManager.detach(entity);
    }
}
