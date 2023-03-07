package com.centralserver.dto.converter;

import com.centralserver.dto.ProductDto;
import com.centralserver.model.enums.ProductStatus;
import com.centralserver.model.Department;
import com.centralserver.model.products.Product;
import com.centralserver.model.products.ProductType;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProductConverter {

    public static ProductDto toProductDto(Product product) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return ProductDto.builder()
                .id(product.getId())
                .serialNumber(product.getSerialNumber())
                .productTypeId(product.getProductType().getId())
                .lastUpdate(formatter.format(product.getLastUpdate().getTime()))
                .status(product.getStatus())
                .departmentId(product.getDepartment().getId())
                .build();
    }


    public static Product toProduct(ProductDto productDto, Product oldProduct, Department department, ProductType productType) {
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
