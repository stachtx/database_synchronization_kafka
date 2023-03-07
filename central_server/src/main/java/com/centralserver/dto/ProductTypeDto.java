package com.centralserver.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ProductTypeDto {

    private UUID id = null;
    private long version;
    private String name;
    private String manufacture;
    private long cost;
}
