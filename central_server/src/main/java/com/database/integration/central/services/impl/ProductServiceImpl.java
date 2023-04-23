package com.database.integration.central.services.impl;

import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.SERIAL_NUMBER_NAME_TAKEN;
import static com.database.integration.core.exception.EntityNotInDatabaseException.ErrorMessage.NO_OBJECT;
import static com.database.integration.core.exception.EntityOptimisticLockException.ErrorMessage.OPTIMISTIC_LOCK;

import com.database.integration.central.kafka.producer.KafkaProducer;
import com.database.integration.central.repositories.DepartmentRepository;
import com.database.integration.central.repositories.ProductRepository;
import com.database.integration.central.repositories.ProductTypeRepository;
import com.database.integration.central.services.ProductService;
import com.database.integration.core.dto.ProductDto;
import com.database.integration.core.dto.converter.ProductMapper;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.model.Department;
import com.database.integration.core.model.Product;
import com.database.integration.core.model.ProductType;
import jakarta.persistence.PersistenceException;
import java.util.List;
import java.util.UUID;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        return products.stream().filter(product -> !product.isDeleted()).toList();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_LIST_FOR_DEPARTMENT_READ')")
    public List<Product> getAllProductsForDepartment(UUID departmentId) {
        List<Product> products = productRepository.findAll().stream()
            .filter(product -> product.getDepartment().getId().equals(departmentId)
                && !product.isDeleted()).toList();
        products.forEach(product -> Hibernate.initialize(product.getProductType()));
        return products;
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    public Product createProduct(ProductDto productDto)
        throws EntityNotInDatabaseException, DatabaseErrorException {
        try {
            Department department = departmentRepository.findById(productDto.getDepartmentId())
                .orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
            ProductType productType = productTypeRepository.findById(productDto.getProductTypeId())
                .orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
            Product savedProduct = productRepository.saveAndFlush(
                ProductMapper.toProduct(productDto, department, productType));
            kafkaProducer.send(savedProduct);
            return savedProduct;
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(SERIAL_NUMBER_NAME_TAKEN);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public Product updateProduct(ProductDto productDto)
        throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException {
        try {
            Product oldProduct = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
            productRepository.detach(oldProduct);
            Department department = departmentRepository.findById(productDto.getDepartmentId())
                .orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
            ProductType productType = productTypeRepository.findById(productDto.getProductTypeId())
                .orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
            Product updatedProduct = ProductMapper.toProduct(productDto, oldProduct, department,
                productType);
            productRepository.saveAndFlush(updatedProduct);
            kafkaProducer.send(updatedProduct);
            return updatedProduct;
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new EntityOptimisticLockException(OPTIMISTIC_LOCK);
        } catch (PersistenceException e) {
            throw new DatabaseErrorException(SERIAL_NUMBER_NAME_TAKEN);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    public void deleteProductById(UUID id) throws EntityNotInDatabaseException {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
        product.setDeleted(true);
        kafkaProducer.send(product);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public Product getProduct(UUID id) throws EntityNotInDatabaseException {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
        Hibernate.initialize(product.getDepartment());
        Hibernate.initialize(product.getProductType());
        return product.isDeleted() ? null : product;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public Product getProductBySerialNumber(String serialNumber) throws EntityNotInDatabaseException {
        Product product = productRepository.findBySerialNumber(serialNumber).orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
        return product.isDeleted() ? null : product;
    }

    @Override
    public void mergeProduct(Product product) {
        productRepository.merge(product);
    }
}
