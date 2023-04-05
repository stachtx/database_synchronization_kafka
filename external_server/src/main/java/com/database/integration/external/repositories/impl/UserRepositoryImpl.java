package com.database.integration.external.repositories.impl;

import com.database.integration.core.model.User;
import com.database.integration.external.repositories.custom_interface.CustomUserRepository;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class UserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void detach(User user) {
        entityManager.detach(user);
    }

    @Override
    public void merge(User user) {
        Session session = (Session) entityManager.getDelegate();
        detach(user);
        session.replicate(user, ReplicationMode.LATEST_VERSION);
    }
}
