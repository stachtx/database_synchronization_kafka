package com.database.integration.central.repositories.custom_interface;

import com.central.model.products.Product;

public interface CustomProductRepository {
    void detach(Product entity);
}
