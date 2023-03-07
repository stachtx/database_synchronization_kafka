package com.database.integration.external.repositories;

import com.database.integration.core.model.Department;
import com.database.integration.external.repositories.custom_interface.CustomDepartmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID>, CustomDepartmentRepository {

}
