package com.database.integration.central.repositories.custom_interface;


import com.database.integration.core.model.Userdata;

public interface CustomUserdataRepository {
    void detach(Userdata entity);
    void merge(Userdata user);
}
