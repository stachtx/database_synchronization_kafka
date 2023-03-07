package com.centralserver.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RegistrationDto {
    private String username;
    private String password;
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

}
