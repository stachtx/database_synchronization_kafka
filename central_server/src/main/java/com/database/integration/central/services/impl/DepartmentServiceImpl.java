package com.database.integration.central.services.impl;

import static com.database.integration.core.dto.converter.DepartmentMapper.toDepartment;
import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.DEPARTMENT_NAME_TAKEN;
import static com.database.integration.core.exception.EntityNotInDatabaseException.ErrorMessage.NO_OBJECT;

import com.database.integration.central.kafka.producer.KafkaProducer;
import com.database.integration.central.repositories.DepartmentRepository;
import com.database.integration.central.services.DepartmentService;
import com.database.integration.core.dto.DepartmentDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.model.Department;
import jakarta.persistence.PersistenceException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    public Department getDepartment(UUID id) throws EntityNotInDatabaseException {
        Department department = departmentRepository.findById(id).orElseThrow(() ->
                new EntityNotInDatabaseException(NO_OBJECT));
        return department.isDeleted() ? null : department;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('DEPARTMENT_LIST_READ')")
    public List<Department> getAll() {
        return departmentRepository.findAll().stream().filter(department -> !department.isDeleted())
            .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('DEPARTMENT_CREATE')")
    public Department createDepartment(DepartmentDto departmentDto) throws DatabaseErrorException {
        try {
            Department savedDepartment = departmentRepository.saveAndFlush(toDepartment(departmentDto));
            kafkaProducer.send(savedDepartment);
            return savedDepartment;
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(DEPARTMENT_NAME_TAKEN);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('DEPARTMENT_UPDATE')")
    public Department updateDepartment(DepartmentDto departmentDto) throws EntityNotInDatabaseException, DatabaseErrorException {
        try {
            Department oldDepartment = departmentRepository.findById(departmentDto.getId()).orElseThrow(() ->
                    new EntityNotInDatabaseException(NO_OBJECT));
            departmentRepository.detach(oldDepartment);
            Department department = departmentRepository.saveAndFlush(toDepartment(departmentDto, oldDepartment));
            kafkaProducer.send(department);
            return department;
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(DEPARTMENT_NAME_TAKEN);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
    public void deleteDepartmentById(UUID id) throws EntityNotInDatabaseException {
        Department department = departmentRepository.findById(id).orElseThrow(() ->
                new EntityNotInDatabaseException(NO_OBJECT));
        department.setDeleted(true);
        kafkaProducer.send(department);
    }

    @Override
    public void mergeDepartment(Department department) {
        departmentRepository.merge(department);
    }
}
