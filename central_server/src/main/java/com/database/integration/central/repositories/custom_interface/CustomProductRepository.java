package com.database.integration.central.repositories.custom_interface;

import com.database.integration.core.model.products.Product;

public interface CustomProductRepository {
    void detach(Product entity);

    void merge(Product entity);
}
