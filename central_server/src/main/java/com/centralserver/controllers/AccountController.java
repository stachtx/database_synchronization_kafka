package com.centralserver.controllers;

import com.centralserver.dto.ProfileEditDto;
import com.centralserver.dto.RegistrationDto;
import com.centralserver.dto.AccountDto;
import com.centralserver.exception.base.SystemBaseException;
import com.centralserver.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/secured/account")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SystemBaseException.class)
public class AccountController {


    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "admin/edit", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> saveAccountAfterEdit(@RequestBody AccountDto data) throws SystemBaseException {
        accountService.updateAccountByAdmin(data);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "admin/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> addUser(@RequestBody RegistrationDto data) throws SystemBaseException {
        accountService.registerNewUserAccount(data, true);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "self/edit", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> safeProfileAfterEdit(@RequestBody ProfileEditDto data)throws SystemBaseException {
        accountService.updateProfileByUser(data);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
