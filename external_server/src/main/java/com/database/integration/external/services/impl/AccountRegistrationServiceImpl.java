package com.database.integration.external.services.impl;

import com.database.integration.core.dto.RegistrationDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.model.Address;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import com.database.integration.core.model.Userdata;
import com.database.integration.external.kafka.producer.KafkaProducer;
import com.database.integration.external.repositories.UserRepository;
import com.database.integration.external.repositories.UserRoleRepository;
import com.database.integration.external.repositories.UserdataRepository;
import com.database.integration.external.services.AccountRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.EMAIL_TAKEN;
import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.USERNAME_TAKEN;

@Service
public class AccountRegistrationServiceImpl implements AccountRegistrationService {

    @Qualifier("userPasswordEncoder")
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserdataRepository userdataRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public void registerNewUserAccount(final RegistrationDto data, boolean verified) throws EntityNotInDatabaseException, DatabaseErrorException {

        validation(data);
        User user = new User();
        user.setUsername(data.getUsername());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        user.setEnabled(true);

        if (data.getRoles() != null) {
            user.setUserRoles(getUserRoles(data));
        }
        Userdata userdata = getUserdata(data);
        getAddress(data, userdata);

        try {
            saveAndFlushAccount(user, userdata);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<UserRole> getUserRoles(RegistrationDto data) {
        return userRoleRepository.findAll().stream().filter(x -> data.getRoles().stream().anyMatch(name -> name.equals(x.getName())))
                .collect(Collectors.toList());
    }

    private void validation(RegistrationDto data) throws DatabaseErrorException {
        if (userdataRepository.findByEmail(data.getEmail()).isPresent()) {
            throw new DatabaseErrorException(EMAIL_TAKEN);
        }
        if (userRepository.findByUsername(data.getUsername()).isPresent()) {
            throw new DatabaseErrorException(USERNAME_TAKEN);
        }
    }

    private static void getAddress(RegistrationDto data, Userdata userdata) {
        Address address = new Address();
        address.setFlatNumber(data.getFlatNumber());
        address.setBuildingNumber(data.getHouseNumber());
        address.setStreet(data.getStreet());
        address.setCity(data.getCity());
        userdata.setAddress(address);
    }

    private static Userdata getUserdata(RegistrationDto data) {
        Userdata userdata = new Userdata();
        userdata.setEmail(data.getEmail());
        userdata.setSurname(data.getSurname());
        userdata.setPosition(data.getPosition());
        userdata.setName(data.getName());
        userdata.setWorkplace(data.getWorkplace());
        userdata.setDateOfJoin(Calendar.getInstance());
        return userdata;
    }

    private void saveAndFlushAccount(User user, Userdata userdata) {
        user.setUserdata(userdata);
        User savedUser = userRepository.saveAndFlush(user);
        kafkaProducer.send(savedUser);
    }
}
