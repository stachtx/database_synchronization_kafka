package com.database.integration.central.repositories.custom_interface;


import com.database.integration.core.model.User;

public interface CustomUserRepository {
    void detach(User entity);
    void merge(User entity);
}
