package com.database.integration.external.services;

import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.exception.ServiceException;
import com.database.integration.core.dto.PasswordInfoDto;
import com.database.integration.core.model.users.User;
import com.database.integration.core.model.users.UserRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    PasswordInfoDto getPasswordForAdmin(String username) throws EntityNotInDatabaseException;

    @Transactional
    @PreAuthorize("hasAuthority('PASSWORD_ADMIN_UPDATE')")
    void updatePasswordForAdmin(PasswordInfoDto passwordInfoForAdmin) throws EntityNotInDatabaseException, EntityOptimisticLockException;

    @Transactional
    @PreAuthorize("hasAuthority('PASSWORD_READ')")
    PasswordInfoDto getPassword(String username) throws EntityNotInDatabaseException;

    @Transactional
    @PreAuthorize("hasAuthority('PASSWORD_UPDATE')")
    void updatePassword(PasswordInfoDto passwordInfoDto, String username) throws EntityNotInDatabaseException, ServiceException;

    @Transactional
    void mergeUser(User user);
}
