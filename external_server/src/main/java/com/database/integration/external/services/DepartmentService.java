package com.database.integration.external.services;

import com.database.integration.core.dto.DepartmentDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.model.Department;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface DepartmentService {

  @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
  Department getDepartment(UUID id) throws EntityNotInDatabaseException;

  @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('DEPARTMENT_LIST_READ')")
  List<Department> getAll();

  @Transactional
  @PreAuthorize("hasAuthority('DEPARTMENT_CREATE')")
  Department createDepartment(DepartmentDto departmentDto) throws DatabaseErrorException;

  @Transactional
  @PreAuthorize("hasAuthority('DEPARTMENT_UPDATE')")
  Department updateDepartment(DepartmentDto departmentDto)
      throws EntityNotInDatabaseException, DatabaseErrorException;

  @Transactional
  @PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
  void deleteDepartmentById(UUID id) throws EntityNotInDatabaseException;

  void mergeDepartment(Department department);

}
