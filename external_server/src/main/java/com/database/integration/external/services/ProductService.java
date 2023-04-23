package com.database.integration.external.services;

import com.database.integration.core.dto.ProductDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.model.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {

  @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_LIST_READ')")
  List<Product> getAllProducts();

  @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_LIST_FOR_DEPARTMENT_READ')")
  List<Product> getAllProductsForDepartment(UUID departmentId);

  @Transactional(propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
  Product createProduct(ProductDto productDto)
      throws DatabaseErrorException, EntityNotInDatabaseException;

  @Transactional(propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
  Product updateProduct(ProductDto product)
      throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException;

  @Transactional(propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
  void deleteProductById(UUID id) throws EntityNotInDatabaseException;

  @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_READ')")
  Product getProduct(UUID id) throws EntityNotInDatabaseException;

  @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_READ')")
  Product getProductBySerialNumber(String serialNumber) throws EntityNotInDatabaseException;

  void mergeProduct(Product product);
}
