package com.database.integration.central.repositories.impl;

import com.database.integration.central.repositories.custom_interface.CustomDepartmentRepository;
import com.central.model.Department;

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
