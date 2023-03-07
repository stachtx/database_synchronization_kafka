package com.database.integration.external.repositories.impl;

import com.database.integration.core.model.users.Userdata;
import com.database.integration.external.repositories.custom_interface.CustomUserdataRepository;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


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
        session.replicate(user, ReplicationMode.LATEST_VERSION);
    }
}
