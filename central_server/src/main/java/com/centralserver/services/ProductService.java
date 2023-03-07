package com.centralserver.services;

import com.centralserver.dto.ProductDto;
import com.centralserver.exception.DatabaseErrorException;
import com.centralserver.exception.EntityNotInDatabaseException;
import com.centralserver.exception.EntityOptimisticLockException;
import com.centralserver.model.products.Product;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_LIST_READ')")
    List<Product> getAllProducts();

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_LIST_FOR_DEPARTMENT_READ')")
    List<Product> getAllProductsForDepartment(UUID departmentId);

    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    void createNewProduct(ProductDto productDto) throws DatabaseErrorException;

    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    void updateProduct(ProductDto product) throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException;

    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    void deleteProductById(UUID id) throws EntityNotInDatabaseException;

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    Product getProduct(UUID id) throws EntityNotInDatabaseException;

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    Product getProductBySerialNumber(String serialNumber) throws EntityNotInDatabaseException;
}
