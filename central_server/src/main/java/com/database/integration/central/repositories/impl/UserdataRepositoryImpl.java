package com.database.integration.central.repositories.impl;

import com.database.integration.central.repositories.custom_interface.CustomUserdataRepository;
import com.database.integration.core.model.Userdata;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;


public class UserdataRepositoryImpl implements CustomUserdataRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void detach(Userdata entity) {
        entityManager.detach(entity);
    }

    @Override
    public void merge(Userdata user) {
        Session session = (Session) entityManager.getDelegate();
        detach(user);
        session.merge(user);
    }
}
