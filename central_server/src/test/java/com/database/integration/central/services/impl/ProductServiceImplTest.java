package com.database.integration.central.services.impl;


import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.SERIAL_NUMBER_NAME_TAKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.database.integration.central.kafka.producer.KafkaProducer;
import com.database.integration.central.repositories.DepartmentRepository;
import com.database.integration.central.repositories.ProductRepository;
import com.database.integration.central.repositories.ProductTypeRepository;
import com.database.integration.core.dto.ProductDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.model.Department;
import com.database.integration.core.model.Product;
import com.database.integration.core.model.ProductStatus;
import com.database.integration.core.model.ProductType;
import jakarta.persistence.PersistenceException;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  @InjectMocks
  ProductServiceImpl productService;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private DepartmentRepository departmentRepository;

  @Mock
  private ProductTypeRepository productTypeRepository;

  @Mock
  private KafkaProducer kafkaProducer;


  Product product;

  ProductDto productDto;

  @BeforeEach
  public void setup() {
    UUID uuid = UUID.randomUUID();
    UUID productTypeId = UUID.randomUUID();
    UUID departmentId = UUID.randomUUID();

    product = Product.builder()
        .id(uuid)
        .serialNumber("serial-number")
        .productType(new ProductType())
        .department(new Department())
        .status(ProductStatus.available)
        .createDate(Calendar.getInstance())
        .lastUpdate(Calendar.getInstance())
        .deleted(false)
        .version(0)
        .build();
    productDto = ProductDto.builder()
        .id(uuid)
        .serialNumber("serial-number")
        .productTypeId(productTypeId)
        .departmentId(departmentId)
        .status(ProductStatus.available)
        .version(0)
        .build();
  }

  @Test
  public void shouldReturnProductByUUID() throws EntityNotInDatabaseException {
    //given
    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
    //when
    Product result = productService.getProduct(product.getId());
    //then
    assertThat(result).isEqualTo(product);
  }

  @Test
  public void shouldThrowExceptionWhenProductIsNotInDatabase() {
    //given
    when(productRepository.findById(product.getId()))
        .thenReturn(Optional.empty());
    //when
    Assertions.assertThatExceptionOfType(EntityNotInDatabaseException.class)
        .isThrownBy(() -> productService.getProduct(product.getId()));
  }

  @Test
  public void shouldCreateProductAndSendToKafkaTopic()
      throws DatabaseErrorException, EntityNotInDatabaseException {
    //give
    when(productTypeRepository.findById(productDto.getProductTypeId())).thenReturn(
        Optional.of(product.getProductType()));
    when(departmentRepository.findById(productDto.getDepartmentId())).thenReturn(
        Optional.of(product.getDepartment()));
    when(productRepository.saveAndFlush(any())).thenReturn(product);
    //when
    Product result = productService.createProduct(productDto);

    //then
    verify(kafkaProducer, times(1)).send(product);
    assertThat(result).isEqualTo(product);
  }

  @Test
  public void shouldThrowExceptionDuringCreateWhenDepartmentNameWasTaken() {
    //given
    when(productRepository.saveAndFlush(any())).thenThrow(new PersistenceException());
    //when
    Assertions.assertThatExceptionOfType(DatabaseErrorException.class)
        .isThrownBy(() -> productService.createProduct(productDto))
        .withMessage(SERIAL_NUMBER_NAME_TAKEN.message);
  }

  @Test
  public void shouldUpdateDepartmentAndSendToKafkaTopic()
      throws DatabaseErrorException, EntityNotInDatabaseException, EntityOptimisticLockException {
    //given
    String updatedSerialNumber = "updated_serial_number";
    productDto.setSerialNumber(updatedSerialNumber);
    Product updateProduct = product;
    updateProduct.setSerialNumber(updatedSerialNumber);
    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
    when(productRepository.saveAndFlush(any())).thenReturn(updateProduct);

    //when
    Product result = productService.updateProduct(productDto);

    //then
    verify(kafkaProducer, times(1)).send(updateProduct);
    assertThat(result).isEqualTo(updateProduct);
  }

  @Test
  public void shouldThrowExceptionWhenDepartmentIsNotInDatabaseDuringUpdate() {
    //given
    String updatedSerialNumber = "updated_serial_number";
    productDto.setSerialNumber(updatedSerialNumber);
    when(productRepository.findById(product.getId()))
        .thenReturn(Optional.empty());
    //when
    Assertions.assertThatExceptionOfType(EntityNotInDatabaseException.class)
        .isThrownBy(() -> productService.updateProduct(productDto));
  }

  @Test
  public void shouldThrowExceptionDuringUpdateWhenDepartmentNameWasTaken() {
    //given
    String updatedSerialNumber = "updated_serial_number";
    productDto.setSerialNumber(updatedSerialNumber);
    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
    when(productRepository.saveAndFlush(any())).thenThrow(new PersistenceException());
    //when
    Assertions.assertThatExceptionOfType(DatabaseErrorException.class)
        .isThrownBy(() -> productService.updateProduct(productDto))
        .withMessage(SERIAL_NUMBER_NAME_TAKEN.message);
  }

  @Test
  public void shouldRemoveDepartmentByUUID() throws EntityNotInDatabaseException {
    //given
    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
    //when
    productService.deleteProductById(product.getId());
    //then
    assertTrue(product.isDeleted());
  }

  @Test
  public void shouldThrowExceptionWhenDepartmentIsNotInDatabaseDuringDelete() {
    //given
    when(productRepository.findById(product.getId()))
        .thenReturn(Optional.empty());
    //when
    Assertions.assertThatExceptionOfType(EntityNotInDatabaseException.class)
        .isThrownBy(() -> productService.deleteProductById(product.getId()));
  }
}