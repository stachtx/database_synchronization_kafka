package com.database.integration.central.repositories.impl;

import com.database.integration.central.repositories.custom_interface.CustomUserRepository;

import com.database.integration.core.model.users.User;
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
