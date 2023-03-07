package com.centralserver.repositories.custom_interface;

import com.centralserver.model.products.Product;

public interface CustomProductRepository {
    void detach(Product entity);
}
