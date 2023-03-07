package com.database.integration.external.services.impl;

import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.dto.DepartmentDto;
import com.database.integration.core.dto.converter.DepartmentConverter;
import com.database.integration.external.kafka.producer.KafkaProducer;
import com.database.integration.core.model.Department;
import com.database.integration.external.repositories.DepartmentRepository;
import com.database.integration.external.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final KafkaProducer kafkaProducer;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    public Department getDepartment(UUID id) throws EntityNotInDatabaseException {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        return department.isDeleted() ? null : department;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('DEPARTMENT_LIST_READ')")
    public List<Department> getAll() {
        return departmentRepository.findAll().stream().filter(department -> !department.isDeleted()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('DEPARTMENT_CREATE')")
    public void createDepartment(DepartmentDto departmentDto) throws DatabaseErrorException {
        try {
            Department department = new Department();
            Department savedDepartment = departmentRepository.saveAndFlush(DepartmentConverter.toDepartment(departmentDto, department));
            kafkaProducer.send(savedDepartment);
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(DatabaseErrorException.DEPARTMENT_NAME_TAKEN);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('DEPARTMENT_UPDATE')")
    public void updateDepartment(DepartmentDto departmentDto) throws EntityNotInDatabaseException, DatabaseErrorException {
        try {
            Department oldDepartment = departmentRepository.findById(departmentDto.getId()).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
            departmentRepository.detach(oldDepartment);
            Department department = DepartmentConverter.toDepartment(departmentDto, oldDepartment);
            departmentRepository.saveAndFlush(department);
            kafkaProducer.send(department);
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(DatabaseErrorException.DEPARTMENT_NAME_TAKEN);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
    public void deleteDepartmentById(UUID id) throws EntityNotInDatabaseException {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        department.setDeleted(true);
        kafkaProducer.send(department);
    }

    @Override
    public void mergeDepartment(Department department) {
        departmentRepository.merge(department);
    }

}
