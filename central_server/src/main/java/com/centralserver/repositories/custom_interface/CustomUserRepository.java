package com.centralserver.repositories.custom_interface;

import com.centralserver.model.users.User;

public interface CustomUserRepository {
    void detach(User entity);

    void merge(User entity);

}
