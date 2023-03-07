package com.centralserver.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ProfileEditDto {
    private UUID id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private String city;
    private String street;
    private String houseNumber;
    private String flatNumber;
    private String position;
    private String workplace;
    private long versionUser;
    private long versionUserdata;
}
