package com.centralserver.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DepartmentDto {
    private UUID id = null;
    private long version;
    private String name;
}
