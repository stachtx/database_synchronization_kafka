package com.database.integration.external.repositories.custom_interface;

import com.database.integration.core.model.users.User;

public interface CustomUserRepository {
    void detach(User entity);

    void merge(User entity);

}
