package com.database.integration.external.controllers;


import com.database.integration.core.dto.PasswordUpdateDto;
import com.database.integration.core.exception.base.SystemBaseException;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import com.database.integration.external.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/secured/users")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SystemBaseException.class)
public class UserController {

    @Autowired
    private UserService userService;

  @GetMapping(value = "/password", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PasswordUpdateDto getPassword() throws SystemBaseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getPassword(auth.getName());
    }

  @GetMapping(value = "/password/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PasswordUpdateDto getPasswordForAdmin(@PathVariable String username)
        throws SystemBaseException {
        return userService.getPasswordForAdmin(username);
    }

  @PutMapping(value = "/password", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public void updatePassword(@RequestBody PasswordUpdateDto passwordUpdateDto)
        throws SystemBaseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.updatePassword(passwordUpdateDto, auth.getName());
    }

  @PutMapping(value = "/password/admin", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public void updatePasswordAdmin(@RequestBody PasswordUpdateDto passwordUpdateDto)
        throws SystemBaseException {
        userService.updatePasswordForAdmin(passwordUpdateDto);
    }

  @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    User getUserEdit(@PathVariable String username) throws SystemBaseException {
        return userService.getUser(username);
    }

  @GetMapping(value = "/user/roles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<UserRole> getUserRoles(@PathVariable String username) throws SystemBaseException {
        return userService.getUserRoles(username);
    }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<User> getAll() {
        return userService.getAllUsers();
    }

  @DeleteMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable String username) throws SystemBaseException {
        userService.deleteUser(username);
    }
}
