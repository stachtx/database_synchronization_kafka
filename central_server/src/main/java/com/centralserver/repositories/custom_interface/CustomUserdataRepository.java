package com.centralserver.repositories.custom_interface;

import com.centralserver.model.users.Userdata;

public interface CustomUserdataRepository {
    void detach(Userdata entity);

    void merge(Userdata user);
}
