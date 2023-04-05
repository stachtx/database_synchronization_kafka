package com.database.integration.core.dto;

import com.database.integration.core.model.ProductStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ProductDto {

    private UUID id = null;
    private long version;
    private String serialNumber;
    private ProductStatus status;
    private UUID productTypeId;
    private String lastUpdate;
    private String createDate;
    private UUID departmentId;
}
