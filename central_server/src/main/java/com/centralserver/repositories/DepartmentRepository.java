package com.centralserver.repositories;

import com.centralserver.model.Department;
import com.centralserver.repositories.custom_interface.CustomDepartmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID>, CustomDepartmentRepository {

}
