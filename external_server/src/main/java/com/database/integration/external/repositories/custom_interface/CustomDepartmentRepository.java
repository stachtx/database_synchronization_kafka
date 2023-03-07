package com.database.integration.external.repositories.custom_interface;

import com.database.integration.core.model.Department;

public interface CustomDepartmentRepository {
    void detach(Department entity);
}
