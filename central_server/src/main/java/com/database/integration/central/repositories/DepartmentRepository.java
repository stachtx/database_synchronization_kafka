package com.database.integration.central.repositories;

import com.database.integration.central.repositories.custom_interface.CustomDepartmentRepository;

import com.database.integration.core.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID>,
    CustomDepartmentRepository {

}
