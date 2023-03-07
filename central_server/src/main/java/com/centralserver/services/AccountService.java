package com.centralserver.services;

import com.centralserver.dto.AccountDto;
import com.centralserver.dto.ProfileEditDto;
import com.centralserver.dto.RegistrationDto;
import com.centralserver.exception.DatabaseErrorException;
import com.centralserver.exception.EntityNotInDatabaseException;
import com.centralserver.exception.EntityOptimisticLockException;
import com.centralserver.exception.base.SystemBaseException;
import com.centralserver.model.users.User;
import com.centralserver.model.users.Userdata;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;


public interface AccountService {

    @Transactional
    @PreAuthorize("hasAuthority('ACCOUNT_UPDATE_ADMIN')")
    void updateAccountByAdmin(AccountDto accountDto) throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException;

    @Transactional
    @PreAuthorize("hasAuthority('USER_CREATE')")
    void registerNewUserAccount(RegistrationDto data, boolean verified) throws EntityNotInDatabaseException, DatabaseErrorException;

    @Transactional
    @PreAuthorize("hasAuthority('ACCOUNT_UPDATE_SELF')")
    void updateProfileByUser(ProfileEditDto profileEditDto) throws SystemBaseException;

    @Transactional
    void mergeUserdata(Userdata userdata);
}
