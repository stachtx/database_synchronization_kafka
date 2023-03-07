package com.database.integration.central.repositories.custom_interface;

import com.central.model.products.ProductType;

public interface CustomProductTypeRepository {

    void detach(ProductType entity);
}
