package com.centralserver.repositories.impl;

import com.centralserver.model.Department;
import com.centralserver.repositories.custom_interface.CustomDepartmentRepository;

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
