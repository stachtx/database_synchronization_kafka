package com.database.integration.core.dto;

import java.io.Serializable;
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
public class UserUpdateDto implements Serializable {

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
