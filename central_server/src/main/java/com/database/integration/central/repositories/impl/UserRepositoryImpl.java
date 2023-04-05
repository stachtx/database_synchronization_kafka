package com.database.integration.central.repositories.impl;

import com.database.integration.central.repositories.custom_interface.CustomUserRepository;
import com.database.integration.core.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;


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
