package com.centralserver.repositories.custom_interface;

import com.centralserver.model.products.ProductType;

public interface CustomProductTypeRepository {

    void detach(ProductType entity);
}
