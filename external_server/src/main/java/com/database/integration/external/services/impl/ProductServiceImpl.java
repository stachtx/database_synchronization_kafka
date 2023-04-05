package com.database.integration.external.services.impl;

import com.database.integration.core.dto.ProductDto;
import com.database.integration.core.dto.converter.ProductConverter;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.model.Department;
import com.database.integration.core.model.Product;
import com.database.integration.core.model.ProductType;
import com.database.integration.external.kafka.producer.KafkaProducer;
import com.database.integration.external.repositories.DepartmentRepository;
import com.database.integration.external.repositories.ProductRepository;
import com.database.integration.external.repositories.ProductTypeRepository;
import com.database.integration.external.services.ProductService;
import jakarta.persistence.PersistenceException;
import org.hibernate.Hibernate;
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
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_LIST_READ')")
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().filter(product -> !product.isDeleted()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_LIST_FOR_DEPARTMENT_READ')")
    public List<Product> getAllProductsForDepartment(UUID departmentId) {
        List<Product> products = productRepository.findAll().stream()
                .filter(product -> product.getDepartment().getId().equals(departmentId) && !product.isDeleted()).collect(Collectors.toList());
        products.forEach(product -> Hibernate.initialize(product.getProductType()));
        return products;
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    public void createNewProduct(ProductDto productDto) throws DatabaseErrorException {
        try {
            Product product = new Product();
            Department department = departmentRepository.getOne(productDto.getDepartmentId());
            ProductType productType = productTypeRepository.getOne(productDto.getProductTypeId());
            Product savedProduct = productRepository.saveAndFlush(ProductConverter.toProduct(productDto, product, department, productType));
            kafkaProducer.send(savedProduct);
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(DatabaseErrorException.SERIAL_NUMBER_NAME_TAKEN);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public void updateProduct(ProductDto product) throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException {
        try {
            Product oldProduct = productRepository.findById(product.getId()).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
            productRepository.detach(oldProduct);
            Department department = departmentRepository.getOne(product.getDepartmentId());
            ProductType productType = productTypeRepository.getOne(product.getProductTypeId());
            Product updatedProduct = ProductConverter.toProduct(product, oldProduct, department, productType);
            productRepository.saveAndFlush(updatedProduct);
            kafkaProducer.send(updatedProduct);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new EntityOptimisticLockException(EntityOptimisticLockException.OPTIMISTIC_LOCK);
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(DatabaseErrorException.SERIAL_NUMBER_NAME_TAKEN);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    public void deleteProductById(UUID id) throws EntityNotInDatabaseException {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        product.setDeleted(true);
        kafkaProducer.send(product);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public Product getProduct(UUID id) throws EntityNotInDatabaseException {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        Hibernate.initialize(product.getDepartment());
        Hibernate.initialize(product.getProductType());
        return product.isDeleted() ? null : product;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public Product getProductBySerialNumber(String serialNumber) throws EntityNotInDatabaseException {
        Product product = productRepository.findBySerialNumber(serialNumber).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        return product.isDeleted() ? null : product;
    }

    @Override
    public void mergeProduct(Product product) {
        productRepository.merge(product);
    }
}
