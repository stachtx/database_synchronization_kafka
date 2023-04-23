package com.database.integration.external.services;

import com.database.integration.core.dto.PasswordUpdateDto;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.exception.ServiceException;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    List<User> getAllUsers();

    @Transactional
    @PreAuthorize("hasAuthority('USER_READ')")
    User getUser(String username) throws EntityNotInDatabaseException;

  @Transactional
  @PreAuthorize("hasAuthority('USER_ROLES_READ')")
  List<UserRole> getUserRoles(String username) throws EntityNotInDatabaseException;

  @Transactional
  @PreAuthorize("hasAuthority('USER_DELETE')")
  void deleteUser(String username) throws EntityNotInDatabaseException;

  @Transactional
  @PreAuthorize("hasAuthority('PASSWORD_ADMIN_READ')")
  PasswordUpdateDto getPasswordForAdmin(String username) throws EntityNotInDatabaseException;

  @Transactional
  @PreAuthorize("hasAuthority('PASSWORD_ADMIN_UPDATE')")
  void updatePasswordForAdmin(PasswordUpdateDto passwordInfoForAdmin)
      throws EntityNotInDatabaseException, EntityOptimisticLockException;

  @Transactional
  @PreAuthorize("hasAuthority('PASSWORD_READ')")
  PasswordUpdateDto getPassword(String username) throws EntityNotInDatabaseException;

  @Transactional
  @PreAuthorize("hasAuthority('PASSWORD_UPDATE')")
  void updatePassword(PasswordUpdateDto passwordUpdateDto, String username)
      throws EntityNotInDatabaseException, ServiceException;

  void mergeUser(User user);
}
