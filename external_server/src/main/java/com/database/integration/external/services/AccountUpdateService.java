package com.database.integration.external.services;

import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.exception.base.SystemBaseException;
import com.database.integration.core.dto.AccountDto;
import com.database.integration.core.model.Userdata;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;


public interface AccountUpdateService {

    @Transactional
    @PreAuthorize("hasAuthority('ACCOUNT_UPDATE_ADMIN')")
    void updateAccountByAdmin(AccountDto accountDto, List<String> roles) throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException;

    @Transactional
    @PreAuthorize("hasAuthority('ACCOUNT_UPDATE_SELF')")
    void updateAccountByUser(AccountDto accountDto) throws SystemBaseException;

    void mergeUserdata(Userdata userdata);
}
