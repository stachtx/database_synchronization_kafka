package com.database.integration.central.services.impl;

import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.DEPARTMENT_NAME_TAKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.database.integration.central.kafka.producer.KafkaProducer;
import com.database.integration.central.repositories.DepartmentRepository;
import com.database.integration.core.dto.DepartmentDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.model.Department;
import jakarta.persistence.PersistenceException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @InjectMocks
    DepartmentServiceImpl departmentService;

    @Mock
    DepartmentRepository departmentRepository;

    @Mock
    KafkaProducer kafkaProducer;

    Department department;

    DepartmentDto departmentDto;

    @BeforeEach
    void setup() {
        UUID uuid = UUID.randomUUID();
        department = Department.builder()
            .id(uuid)
            .name("department")
            .version(0)
            .products(Collections.emptySet())
            .build();
        departmentDto = DepartmentDto.builder()
            .id(uuid)
            .name("department")
                .version(0)
                .build();
    }

    @Test
    void shouldReturnDepartmentByUUID() throws EntityNotInDatabaseException {
        //given
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        //when
        Department result = departmentService.getDepartment(department.getId());
        //then
        assertThat(result).isEqualTo(department);
    }

    @Test
    void shouldThrowExceptionWhenDepartmentIsNotInDatabase() {
        //given
        when(departmentRepository.findById(department.getId()))
            .thenReturn(Optional.empty());
        //when
        Assertions.assertThatExceptionOfType(EntityNotInDatabaseException.class)
            .isThrownBy(() -> departmentService.getDepartment(department.getId()));
    }

    @Test
    void shouldCreateDepartmentAndSendToKafkaTopic() throws DatabaseErrorException {
        //give
        when(departmentRepository.saveAndFlush(any())).thenReturn(department);
        //when
        Department result = departmentService.createDepartment(departmentDto);

        //then
        verify(kafkaProducer, times(1)).send(department);
        assertThat(result).isEqualTo(department);
    }

    @Test
    void shouldThrowExceptionDuringCreateWhenDepartmentNameWasTaken() {
        //given
        when(departmentRepository.saveAndFlush(any())).thenThrow(new PersistenceException());
        //when
        Assertions.assertThatExceptionOfType(DatabaseErrorException.class)
            .isThrownBy(() -> departmentService.createDepartment(departmentDto))
            .withMessage(DEPARTMENT_NAME_TAKEN.message);
    }

    @Test
    void shouldUpdateDepartmentAndSendToKafkaTopic()
        throws DatabaseErrorException, EntityNotInDatabaseException {
        //given
        String newName = "Updated_name";
        departmentDto.setName(newName);
        Department updateDepartment = department;
        updateDepartment.setName(newName);
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(departmentRepository.saveAndFlush(any())).thenReturn(updateDepartment);

        //when
        Department result = departmentService.updateDepartment(departmentDto);

        //then
        verify(kafkaProducer, times(1)).send(department);
        assertThat(result.getName()).isEqualTo(newName);
    }

    @Test
    void shouldThrowExceptionWhenDepartmentIsNotInDatabaseDuringUpdate() {
        //given
        String newName = "Updated_name";
        departmentDto.setName(newName);
        when(departmentRepository.findById(department.getId()))
            .thenReturn(Optional.empty());
        //when
        Assertions.assertThatExceptionOfType(EntityNotInDatabaseException.class)
            .isThrownBy(() -> departmentService.updateDepartment(departmentDto));
    }

    @Test
    void shouldThrowExceptionDuringUpdateWhenDepartmentNameWasTaken() {
        //given
        String newName = "Updated_name";
        departmentDto.setName(newName);
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(departmentRepository.saveAndFlush(any())).thenThrow(new PersistenceException());
        //when
        Assertions.assertThatExceptionOfType(DatabaseErrorException.class)
            .isThrownBy(() -> departmentService.updateDepartment(departmentDto))
            .withMessage(DEPARTMENT_NAME_TAKEN.message);
    }

    @Test
    void shouldRemoveDepartmentByUUID() throws EntityNotInDatabaseException {
        //given
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        //when
        departmentService.deleteDepartmentById(department.getId());
        //then
        assertTrue(department.isDeleted());
    }

    @Test
    void shouldThrowExceptionWhenDepartmentIsNotInDatabaseDuringDelete() {
        //given
        when(departmentRepository.findById(department.getId()))
            .thenReturn(Optional.empty());
        //when
        Assertions.assertThatExceptionOfType(EntityNotInDatabaseException.class)
            .isThrownBy(() -> departmentService.deleteDepartmentById(department.getId()));
    }
}