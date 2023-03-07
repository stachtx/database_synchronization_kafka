package com.centralserver.services;

import com.centralserver.dto.ProductTypeDto;
import com.centralserver.exception.DatabaseErrorException;
import com.centralserver.exception.EntityNotInDatabaseException;
import com.centralserver.exception.EntityOptimisticLockException;
import com.centralserver.exception.base.SystemBaseException;
import com.centralserver.model.products.ProductType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ProductTypeService {

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_READ')")
    ProductType getProductTypeById(UUID id) throws EntityNotInDatabaseException;

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_LIST_READ')")
    List<ProductType> getAllProductTypes();

    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_CREATE')")
    void createNewDeviceModel(ProductTypeDto productTypeDto) throws DatabaseErrorException;

    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_UPDATE')")
    void updateProductType(ProductTypeDto productTypeDto) throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException;

    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_DELETE')")
    void deleteProductTypeById(UUID id) throws EntityNotInDatabaseException;
}
