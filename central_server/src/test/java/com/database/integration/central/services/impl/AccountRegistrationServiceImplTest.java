package com.database.integration.central.services.impl;

import com.database.integration.central.kafka.producer.KafkaProducer;
import com.database.integration.central.repositories.UserRepository;
import com.database.integration.central.repositories.UserRoleRepository;
import com.database.integration.central.repositories.UserdataRepository;
import com.database.integration.core.dto.RegistrationDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.model.Authority;
import com.database.integration.core.model.UserRole;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountRegistrationServiceImplTest {

    @InjectMocks
    AccountRegistrationServiceImpl accountRegistrationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserdataRepository userdataRepository;

    @Mock
    private KafkaProducer kafkaProducer;


    @Test
    @Tag("New account registration test")
    void ShouldRegisterNewAccount() throws DatabaseErrorException, EntityNotInDatabaseException {
        //given
        RegistrationDto registrationDto = prepareRegistrationDTO();
        String encodedPassword = Base64.getEncoder().encodeToString(registrationDto.getPassword().getBytes());
        List<Authority> authorities = List.of(new Authority(UUID.randomUUID(), "READ_DEPARTMENT", true));
        UserRole userRole = new UserRole(UUID.randomUUID(), "user", true, authorities);
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn(encodedPassword);
        when(userRoleRepository.findAll()).thenReturn(List.of(userRole));
        //when
        accountRegistrationService.registerNewUserAccount(registrationDto, true);
        //then
    }

    private RegistrationDto prepareRegistrationDTO() {
        return RegistrationDto.builder()
                .username("username")
                .password("password")
                .email("")
                .name("name")
                .surname("surname")
                .city("city")
                .street("street")
                .houseNumber("6")
                .flatNumber("7")
                .position("position")
                .workplace("workplace")
                .roles(List.of("user"))
                .build();
    }

}