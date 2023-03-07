package com.database.integration.central.controllers;

import com.database.integration.central.services.AccountRegistrationService;
import com.database.integration.central.services.AccountUpdateService;
import com.database.integration.core.dto.AccountDto;
import com.database.integration.core.dto.RegistrationDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.base.SystemBaseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/secured/account")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SystemBaseException.class)
public class AccountController {

  @Autowired
  private AccountUpdateService accountUpdateService;
  @Autowired
  private AccountRegistrationService accountRegistrationService;

  @RequestMapping(value = "admin/edit", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<?> saveAccountAfterEdit(@RequestBody AccountDto data,
      @RequestBody List<String> roles)
      throws SystemBaseException {
    accountUpdateService.updateAccountByAdmin(data, roles);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @RequestMapping(value = "admin/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<?> addUser(@RequestBody RegistrationDto data)
      throws SystemBaseException {
    accountRegistrationService.registerNewUserAccount(data, true);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @RequestMapping(value = "self/edit", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<?> safeProfileAfterEdit(@RequestBody AccountDto data)
      throws SystemBaseException {
    accountUpdateService.updateAccountByUser(data);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
