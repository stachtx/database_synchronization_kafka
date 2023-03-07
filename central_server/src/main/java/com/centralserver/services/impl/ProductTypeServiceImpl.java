package com.centralserver.services.impl;

import com.centralserver.dto.ProductTypeDto;
import com.centralserver.dto.converter.ProductTypeConverter;
import com.centralserver.exception.DatabaseErrorException;
import com.centralserver.exception.EntityNotInDatabaseException;
import com.centralserver.exception.EntityOptimisticLockException;
import com.centralserver.kafka.producer.KafkaProducer;
import com.centralserver.model.products.ProductType;
import com.centralserver.repositories.ProductTypeRepository;
import com.centralserver.services.ProductTypeService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
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
        ProductType productType = productTypeRepository.findById(id).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        return productType.isDeleted() ? null : productType;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_LIST_READ')")
    public List<ProductType> getAllProductTypes() {
        List<ProductType> deviceModels = Lists.newArrayList(productTypeRepository.findAll());
        return deviceModels.stream().filter(productType -> !productType.isDeleted()).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_CREATE')")
    public void createNewDeviceModel(ProductTypeDto productTypeDto) throws DatabaseErrorException {
        try {
            ProductType productType = new ProductType();
            ProductType savedProductType = productTypeRepository.saveAndFlush(ProductTypeConverter.toProductType(productTypeDto, productType));
            kafkaProducer.send(savedProductType);
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(DatabaseErrorException.PRODUCT_TYPE_NAME_NAME_TAKEN);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_TYPE_UPDATE')")
    public void updateProductType(ProductTypeDto productTypeDto) throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException {
        try {
            ProductType oldProductType = productTypeRepository.findById(productTypeDto.getId()).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
            productTypeRepository.detach(oldProductType);
            ProductType updatedProductType = ProductTypeConverter.toProductType(productTypeDto, oldProductType);
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
        ProductType productType = productTypeRepository.findById(id).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        productType.setDeleted(true);
        kafkaProducer.send(productType);
    }

}
