package com.database.integration.external.controllers;

import com.database.integration.core.dto.UserRegistrationDto;
import com.database.integration.core.dto.UserUpdateDto;
import com.database.integration.core.exception.base.SystemBaseException;
import com.database.integration.external.services.UserRegistrationService;
import com.database.integration.external.services.UserUpdateService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/secured/account")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SystemBaseException.class)
public class AccountController {

    @Autowired
    private UserUpdateService userUpdateService;
    @Autowired
    private UserRegistrationService userRegistrationService;

  @PutMapping(value = "admin/edit", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> saveAccountAfterEdit(@RequestBody UserUpdateDto data,
        @RequestBody List<String> roles) throws SystemBaseException {
        userUpdateService.updateAccountByAdmin(data, roles);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

  @PostMapping(value = "admin/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> addUser(@RequestBody UserRegistrationDto data)
        throws SystemBaseException {
        userRegistrationService.registerNewUserAccount(data, true);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

  @PutMapping(value = "self/edit", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> safeProfileAfterEdit(@RequestBody UserUpdateDto data)
        throws SystemBaseException {
        userUpdateService.updateAccountByUser(data);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
