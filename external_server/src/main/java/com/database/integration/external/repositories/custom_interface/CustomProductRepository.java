package com.database.integration.external.repositories.custom_interface;

import com.database.integration.core.model.products.Product;

public interface CustomProductRepository {
    void detach(Product entity);
}
