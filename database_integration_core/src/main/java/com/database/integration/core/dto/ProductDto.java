package com.database.integration.core.dto;

import com.database.integration.core.model.ProductStatus;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ProductDto {

    private UUID id;
    private long version;
    private String serialNumber;
    private ProductStatus status;
    private UUID productTypeId;
    private UUID departmentId;
}
