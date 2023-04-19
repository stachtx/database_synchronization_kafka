package com.database.integration.core.dto.converter;

import com.database.integration.core.dto.ProductDto;
import com.database.integration.core.model.Department;
import com.database.integration.core.model.Product;
import com.database.integration.core.model.ProductStatus;
import com.database.integration.core.model.ProductType;
import java.util.Calendar;

public class ProductMapper {

    public static ProductDto toProductDto(Product product) {
        return ProductDto.builder()
            .id(product.getId())
            .serialNumber(product.getSerialNumber())
            .productTypeId(product.getProductType().getId())
            .status(product.getStatus())
            .departmentId(product.getDepartment().getId())
            .build();
    }

    public static Product toProduct(ProductDto productDto, Department department,
        ProductType productType) {
        return Product.builder().status(ProductStatus.available)
            .department(department)
            .deleted(false)
            .serialNumber(productDto.getSerialNumber())
            .lastUpdate(Calendar.getInstance())
            .createDate(Calendar.getInstance())
            .productType(productType).build();
    }

    public static Product toProduct(ProductDto productDto, Product oldProduct,
        Department department, ProductType productType) {
        oldProduct.setStatus(ProductStatus.available);
        oldProduct.setDepartment(department);
        oldProduct.setDeleted(false);
        oldProduct.setSerialNumber(productDto.getSerialNumber());
        oldProduct.setLastUpdate(Calendar.getInstance());
        oldProduct.setCreateDate(Calendar.getInstance());
        oldProduct.setProductType(productType);
        return oldProduct;
    }
}
