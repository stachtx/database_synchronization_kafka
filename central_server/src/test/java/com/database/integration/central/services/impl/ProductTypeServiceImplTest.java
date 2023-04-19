package com.database.integration.central.services.impl;

import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.PRODUCT_TYPE_NAME_TAKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.database.integration.central.kafka.producer.KafkaProducer;
import com.database.integration.central.repositories.ProductTypeRepository;
import com.database.integration.core.dto.ProductTypeDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.model.ProductType;
import jakarta.persistence.PersistenceException;
import java.util.Collections;
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
class ProductTypeServiceImplTest {

  @InjectMocks
  ProductTypeServiceImpl productTypeService;

  @Mock
  private ProductTypeRepository productTypeRepository;

  @Mock
  private KafkaProducer kafkaProducer;

  ProductType productType;

  ProductTypeDto productTypeDto;

  @BeforeEach
  public void setup() {
    UUID uuid = UUID.randomUUID();
    productType = ProductType.builder()
        .id(uuid)
        .name("product-type")
        .manufacture("manufacture")
        .products(Collections.emptySet())
        .cost(100)
        .deleted(false)
        .version(0)
        .build();
    productTypeDto = ProductTypeDto.builder()
        .id(uuid)
        .name("product-type")
        .manufacture("manufacture")
        .cost(100)
        .version(0)
        .build();
  }

  @Test
  void shouldReturnProductTypeByUUID() throws EntityNotInDatabaseException {
    //given
    when(productTypeRepository.findById(productType.getId())).thenReturn(Optional.of(productType));
    //when
    ProductType result = productTypeService.getProductTypeById(productType.getId());
    //then
    assertThat(result).isEqualTo(productType);
  }

  @Test
  void shouldThrowExceptionWhenProductTypeIsNotInDatabase() {
    //given
    when(productTypeRepository.findById(productType.getId()))
        .thenReturn(Optional.empty());
    //when
    Assertions.assertThatExceptionOfType(EntityNotInDatabaseException.class)
        .isThrownBy(() -> productTypeService.getProductTypeById(productType.getId()));
  }

  @Test
  void shouldCreateProductTypeAndSendToKafkaTopic()
      throws DatabaseErrorException, EntityNotInDatabaseException {
    //give
    when(productTypeRepository.saveAndFlush(any())).thenReturn(productType);
    //when
    ProductType result = productTypeService.createProductType(productTypeDto);

    //then
    verify(kafkaProducer, times(1)).send(productType);
    assertThat(result).isEqualTo(productType);
  }

  @Test
  void shouldThrowExceptionDuringCreateWhenDepartmentNameWasTaken() {
    //given
    when(productTypeRepository.saveAndFlush(any())).thenThrow(new PersistenceException());
    //when
    Assertions.assertThatExceptionOfType(DatabaseErrorException.class)
        .isThrownBy(() -> productTypeService.createProductType(productTypeDto))
        .withMessage(PRODUCT_TYPE_NAME_TAKEN.message);
  }

  @Test
  void shouldUpdateDepartmentAndSendToKafkaTopic()
      throws DatabaseErrorException, EntityNotInDatabaseException, EntityOptimisticLockException {
    //given
    String updatedSerialNumber = "updated_serial_number";
    productTypeDto.setName(updatedSerialNumber);
    ProductType updateProductType = productType;
    updateProductType.setName(updatedSerialNumber);
    when(productTypeRepository.findById(productType.getId())).thenReturn(Optional.of(productType));
    when(productTypeRepository.saveAndFlush(any())).thenReturn(updateProductType);

    //when
    ProductType result = productTypeService.updateProductType(productTypeDto);

    //then
    verify(kafkaProducer, times(1)).send(updateProductType);
    assertThat(result).isEqualTo(updateProductType);
  }

  @Test
  void shouldThrowExceptionWhenDepartmentIsNotInDatabaseDuringUpdate() {
    //given
    String updatedSerialNumber = "updated_serial_number";
    productTypeDto.setName(updatedSerialNumber);
    when(productTypeRepository.findById(productType.getId()))
        .thenReturn(Optional.empty());
    //when
    Assertions.assertThatExceptionOfType(EntityNotInDatabaseException.class)
        .isThrownBy(() -> productTypeService.updateProductType(productTypeDto));
  }

  @Test
  void shouldThrowExceptionDuringUpdateWhenDepartmentNameWasTaken() {
    //given
    String updatedSerialNumber = "updated_serial_number";
    productTypeDto.setName(updatedSerialNumber);
    when(productTypeRepository.findById(productType.getId())).thenReturn(Optional.of(productType));
    when(productTypeRepository.saveAndFlush(any())).thenThrow(new PersistenceException());
    //when
    Assertions.assertThatExceptionOfType(DatabaseErrorException.class)
        .isThrownBy(() -> productTypeService.updateProductType(productTypeDto))
        .withMessage(PRODUCT_TYPE_NAME_TAKEN.message);
  }

  @Test
  void shouldRemoveDepartmentByUUID() throws EntityNotInDatabaseException {
    //given
    when(productTypeRepository.findById(productType.getId())).thenReturn(Optional.of(productType));
    //when
    productTypeService.deleteProductTypeById(productType.getId());
    //then
    assertTrue(productType.isDeleted());
  }

  @Test
  void shouldThrowExceptionWhenDepartmentIsNotInDatabaseDuringDelete() {
    //given
    when(productTypeRepository.findById(productType.getId()))
        .thenReturn(Optional.empty());
    //when
    Assertions.assertThatExceptionOfType(EntityNotInDatabaseException.class)
        .isThrownBy(() -> productTypeService.deleteProductTypeById(productType.getId()));
  }
}