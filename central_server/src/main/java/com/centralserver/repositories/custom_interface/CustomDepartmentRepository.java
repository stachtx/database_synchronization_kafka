package com.centralserver.repositories.custom_interface;

import com.centralserver.model.Department;

public interface CustomDepartmentRepository {
    void detach(Department entity);
}
