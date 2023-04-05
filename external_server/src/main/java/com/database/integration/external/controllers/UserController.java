package com.database.integration.external.controllers;


import com.database.integration.core.dto.PasswordInfoDto;
import com.database.integration.core.exception.base.SystemBaseException;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import com.database.integration.external.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/secured/users")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SystemBaseException.class)
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/password", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PasswordInfoDto getPassword() throws SystemBaseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getPassword(auth.getName());
    }

    @RequestMapping(value = "/password/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PasswordInfoDto getPasswordForAdmin(@PathVariable String username) throws SystemBaseException {
        return userService.getPasswordForAdmin(username);
    }

    @RequestMapping(value = "/password", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePassword(@RequestBody PasswordInfoDto passwordInfoDto) throws SystemBaseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.updatePassword(passwordInfoDto, auth.getName());
    }

    @RequestMapping(value = "/password/admin", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePasswordAdmin(@RequestBody PasswordInfoDto passwordInfoDto) throws SystemBaseException {
        userService.updatePasswordForAdmin(passwordInfoDto);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    User getUserEdit(@PathVariable String username) throws SystemBaseException {
        return userService.getUser(username);
    }

    @RequestMapping(value = "/user/roles/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<UserRole> getUserRoles(@PathVariable String username) throws SystemBaseException {
        return userService.getUserRoles(username);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<User> getAll() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable String username) throws SystemBaseException {
        userService.deleteUser(username);
    }
}
