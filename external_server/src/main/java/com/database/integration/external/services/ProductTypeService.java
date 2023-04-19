package com.database.integration.external.services;

import com.database.integration.core.dto.ProductTypeDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.model.ProductType;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ProductTypeService {

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_READ')")
    ProductType getProductTypeById(UUID id) throws EntityNotInDatabaseException;

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_LIST_READ')")
    List<ProductType> getAllProductTypes();

    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_CREATE')")
    ProductType createProductType(ProductTypeDto productTypeDto) throws DatabaseErrorException;

    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_UPDATE')")
    ProductType updateProductType(ProductTypeDto productTypeDto)
        throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException;

    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_DELETE')")
    void deleteProductTypeById(UUID id) throws EntityNotInDatabaseException;

    void mergeProductType(ProductType productType);
}
