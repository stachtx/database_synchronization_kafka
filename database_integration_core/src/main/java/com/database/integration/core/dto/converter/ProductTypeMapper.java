package com.database.integration.core.dto.converter;

import com.database.integration.core.dto.ProductTypeDto;
import com.database.integration.core.model.ProductType;

public interface ProductTypeMapper {

  static ProductTypeDto toProductTypeDto(ProductType productType) {
    return ProductTypeDto.builder()
        .id(productType.getId())
        .manufacture(productType.getManufacture())
        .name(productType.getName())
        .cost(productType.getCost())
        .version(productType.getVersion())
        .build();
  }

  static ProductType toProductType(ProductTypeDto productTypeDto) {
    return ProductType.builder()
        .cost(productTypeDto.getCost())
        .manufacture(productTypeDto.getManufacture())
        .name(productTypeDto.getName())
        .version(productTypeDto.getVersion())
        .build();
  }

  static ProductType toProductType(ProductTypeDto productTypeDto,
      ProductType oldProductType) {
    oldProductType.setCost(productTypeDto.getCost());
    oldProductType.setManufacture(productTypeDto.getManufacture());
    oldProductType.setName(productTypeDto.getName());
    oldProductType.setVersion(productTypeDto.getVersion());
    return oldProductType;
  }

}
