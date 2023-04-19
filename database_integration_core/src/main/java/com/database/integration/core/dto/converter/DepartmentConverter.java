package com.database.integration.core.dto.converter;

import com.database.integration.core.dto.DepartmentDto;
import com.database.integration.core.model.Department;

public class DepartmentConverter {

    public static DepartmentDto toDepartmentViewDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .version(department.getVersion())
                .build();
    }

    public static Department toDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setName(departmentDto.getName());
        department.setVersion(departmentDto.getVersion());
        return department;
    }

    public static Department toDepartment(DepartmentDto departmentDto, Department oldDepartment) {
        oldDepartment.setName(departmentDto.getName());
        oldDepartment.setVersion(departmentDto.getVersion());
        return oldDepartment;
    }
}

