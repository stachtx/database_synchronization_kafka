package com.database.integration.external.controllers;

import com.database.integration.core.dto.DepartmentDto;
import com.database.integration.core.exception.base.SystemBaseException;
import com.database.integration.core.model.Department;
import com.database.integration.external.services.DepartmentService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/secured/departments")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SystemBaseException.class)
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Department get(@PathVariable UUID id) throws SystemBaseException {
        return departmentService.getDepartment(id);
    }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Department> getAll() {
        return departmentService.getAll();
    }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> create(@RequestBody DepartmentDto departmentDto) throws SystemBaseException {
        departmentService.createDepartment(departmentDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public void update(@RequestBody DepartmentDto departmentDto) throws SystemBaseException {
        departmentService.updateDepartment(departmentDto);
    }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id) throws SystemBaseException {
        departmentService.deleteDepartmentById(id);
    }
}
