package com.database.integration.central.services;

import com.database.integration.core.dto.RegistrationDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRegistrationService {

  @Transactional
  @PreAuthorize("hasAuthority('USER_CREATE')")
  void registerNewUserAccount(
      RegistrationDto data, boolean verified)
      throws EntityNotInDatabaseException, DatabaseErrorException;
}
