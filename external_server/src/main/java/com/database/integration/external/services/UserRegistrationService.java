package com.database.integration.external.services;

import com.database.integration.core.dto.UserRegistrationDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

public interface UserRegistrationService {

  @Transactional
  @PreAuthorize("hasAuthority('USER_CREATE')")
  User registerNewUserAccount(
      UserRegistrationDto data, boolean verified)
      throws EntityNotInDatabaseException, DatabaseErrorException;
}
