package com.database.integration.external.services.impl;

import static com.database.integration.core.dto.converter.ProductTypeMapper.toProductType;
import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.PRODUCT_TYPE_NAME_TAKEN;
import static com.database.integration.core.exception.EntityNotInDatabaseException.ErrorMessage.NO_OBJECT;
import static com.database.integration.core.exception.EntityOptimisticLockException.ErrorMessage.OPTIMISTIC_LOCK;

import com.database.integration.core.dto.ProductTypeDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.model.ProductType;
import com.database.integration.external.kafka.producer.KafkaProducer;
import com.database.integration.external.repositories.ProductTypeRepository;
import com.database.integration.external.services.ProductTypeService;
import jakarta.persistence.PersistenceException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

  @Autowired
  private ProductTypeRepository productTypeRepository;

  @Autowired
  private KafkaProducer kafkaProducer;

  @Override
  @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_TYPE_READ')")
  public ProductType getProductTypeById(UUID id) throws EntityNotInDatabaseException {
    var productType = productTypeRepository.findById(id).orElseThrow(
        () -> new EntityNotInDatabaseException(NO_OBJECT));
    return productType.isDeleted() ? null : productType;
  }

  @Override
  @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_TYPE_LIST_READ')")
  public List<ProductType> getAllProductTypes() {
    var deviceModels = productTypeRepository.findAll();
    return deviceModels.stream().filter(productType -> !productType.isDeleted()).toList();
  }

  @Override
  @Transactional(propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_TYPE_CREATE')")
  public ProductType createProductType(ProductTypeDto productTypeDto)
      throws DatabaseErrorException {
    try {
      var savedProductType = productTypeRepository.saveAndFlush(
          toProductType(productTypeDto));
      kafkaProducer.send(savedProductType);
      return savedProductType;
    } catch (PersistenceException e) {
      throw new DatabaseErrorException(PRODUCT_TYPE_NAME_TAKEN);
    }
  }

  @Override
  @Transactional(propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_TYPE_UPDATE')")
  public ProductType updateProductType(ProductTypeDto productTypeDto)
      throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException {
    try {
      var oldProductType = productTypeRepository.findById(productTypeDto.getId())
          .orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
      productTypeRepository.detach(oldProductType);
      var updatedProductType = productTypeRepository.saveAndFlush(
          toProductType(productTypeDto, oldProductType));
      kafkaProducer.send(updatedProductType);
      return updatedProductType;
    } catch (ObjectOptimisticLockingFailureException e) {
      e.printStackTrace();
      throw new EntityOptimisticLockException(OPTIMISTIC_LOCK);
    } catch (PersistenceException e) {
      throw new DatabaseErrorException(PRODUCT_TYPE_NAME_TAKEN);
    }
  }

  @Override
  @Transactional(propagation = Propagation.MANDATORY)
  @PreAuthorize("hasAuthority('PRODUCT_TYPE_DELETE')")
  public void deleteProductTypeById(UUID id) throws EntityNotInDatabaseException {
    var productType = productTypeRepository.findById(id).orElseThrow(
        () -> new EntityNotInDatabaseException(NO_OBJECT));
    productType.setDeleted(true);
    kafkaProducer.send(productType);
  }

  @Override
  public void mergeProductType(ProductType productType) {
    productTypeRepository.merge(productType);
  }
}
