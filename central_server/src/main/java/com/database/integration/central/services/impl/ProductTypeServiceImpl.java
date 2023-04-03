package com.database.integration.central.services.impl;

import com.database.integration.central.kafka.producer.KafkaProducer;
import com.database.integration.central.repositories.ProductTypeRepository;
import com.database.integration.central.services.ProductTypeService;
import com.database.integration.core.dto.ProductTypeDto;
import com.database.integration.core.dto.converter.ProductTypeConverter;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.model.products.ProductType;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        ProductType productType = productTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        return productType.isDeleted() ? null : productType;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_LIST_READ')")
    public List<ProductType> getAllProductTypes() {
        List<ProductType> deviceModels = productTypeRepository.findAll();
        return deviceModels.stream().filter(productType -> !productType.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_CREATE')")
    public void createNewDeviceModel(ProductTypeDto productTypeDto) throws DatabaseErrorException {
        try {
            ProductType productType = new ProductType();
            ProductType savedProductType = productTypeRepository.saveAndFlush(
                    ProductTypeConverter.toProductType(productTypeDto, productType));
            kafkaProducer.send(savedProductType);
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(DatabaseErrorException.PRODUCT_TYPE_NAME_NAME_TAKEN);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_UPDATE')")
    public void updateProductType(ProductTypeDto productTypeDto)
            throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException {
        try {
            ProductType oldProductType = productTypeRepository.findById(productTypeDto.getId())
                    .orElseThrow(
                            () -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
            productTypeRepository.detach(oldProductType);
            ProductType updatedProductType = ProductTypeConverter.toProductType(productTypeDto,
                    oldProductType);
            productTypeRepository.saveAndFlush(updatedProductType);
            kafkaProducer.send(updatedProductType);
        } catch (ObjectOptimisticLockingFailureException e) {
            e.printStackTrace();
            throw new EntityOptimisticLockException(EntityOptimisticLockException.OPTIMISTIC_LOCK);
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(DatabaseErrorException.PRODUCT_TYPE_NAME_NAME_TAKEN);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_DELETE')")
    public void deleteProductTypeById(UUID id) throws EntityNotInDatabaseException {
        ProductType productType = productTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        productType.setDeleted(true);
        kafkaProducer.send(productType);
    }

    @Override
    public void mergeProductType(ProductType productType) {
        productTypeRepository.merge(productType);
    }
}
