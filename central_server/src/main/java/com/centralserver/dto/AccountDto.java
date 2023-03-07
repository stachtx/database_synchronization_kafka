package com.centralserver.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AccountDto implements Serializable {
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
    private List<String> roles;
    private long versionUser;
    private long versionUserdata;

}
