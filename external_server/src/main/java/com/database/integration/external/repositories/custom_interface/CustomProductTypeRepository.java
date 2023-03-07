package com.database.integration.external.repositories.custom_interface;

import com.database.integration.core.model.products.ProductType;

public interface CustomProductTypeRepository {

  void detach(ProductType entity);

  void merge(ProductType productType);
}
