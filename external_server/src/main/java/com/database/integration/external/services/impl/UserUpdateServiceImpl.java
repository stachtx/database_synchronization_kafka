package com.database.integration.external.services.impl;

import static com.database.integration.core.dto.converter.UserMapper.toUser;
import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.EMAIL_TAKEN;
import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.USERNAME_TAKEN;
import static com.database.integration.core.exception.EntityNotInDatabaseException.ErrorMessage.NO_OBJECT;
import static com.database.integration.core.exception.EntityOptimisticLockException.ErrorMessage.OPTIMISTIC_LOCK;

import com.database.integration.core.dto.UserUpdateDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import com.database.integration.core.model.Userdata;
import com.database.integration.external.kafka.producer.KafkaProducer;
import com.database.integration.external.repositories.UserRepository;
import com.database.integration.external.repositories.UserRoleRepository;
import com.database.integration.external.repositories.UserdataRepository;
import com.database.integration.external.services.UserUpdateService;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserUpdateServiceImpl implements UserUpdateService {

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
    public void mergeUserdata(Userdata userdata) {
        userdataRepository.merge(userdata);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ACCOUNT_UPDATE_ADMIN')")
    public void updateAccountByAdmin(UserUpdateDto userUpdateDto, List<String> roles)
        throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException {

        var user = userRepository.findById(userUpdateDto.getId()).orElseThrow(
            (() -> new EntityNotInDatabaseException(NO_OBJECT)));
        validation(userUpdateDto, user);
        userRepository.detach(user);
        try {
            var userRoles = getUserRoles(roles);

            var mappedUser = toUser(userUpdateDto, user);
            mappedUser.setUserRoles(userRoles);
            var updateUser = userRepository.saveAndFlush(mappedUser);
            kafkaProducer.send(updateUser);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new EntityOptimisticLockException(OPTIMISTIC_LOCK);
        }

    }

    private Set<UserRole> getUserRoles(List<String> roles) {
        if (roles != null && !roles.isEmpty()) {
            return userRoleRepository.findAll().stream()
                .filter(role -> roles.stream().anyMatch(name -> name.equals(role.getName())))
                .collect(
                    Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ACCOUNT_UPDATE_SELF')")
    public void updateAccountByUser(UserUpdateDto userUpdateDto)
        throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException {
        var user = userRepository.findById(userUpdateDto.getId()).orElseThrow(
            (() -> new EntityNotInDatabaseException(NO_OBJECT)));
        validation(userUpdateDto, user);
        userRepository.detach(user);
        try {
            var updatedUser = userRepository.saveAndFlush(toUser(userUpdateDto, user));
            kafkaProducer.send(updatedUser);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new EntityOptimisticLockException(OPTIMISTIC_LOCK);
        }
    }

    private void validation(UserUpdateDto userUpdateDto, User user)
        throws DatabaseErrorException {
        if (userdataRepository.findByEmail(userUpdateDto.getEmail()).isPresent()
            && !user.getUserdata()
            .getEmail()
            .equals(userUpdateDto.getEmail())) {
            throw new DatabaseErrorException(EMAIL_TAKEN);
        }
        if (userRepository.findByUsername(userUpdateDto.getUsername()).isPresent()
            && !user.getUsername().equals(
            userUpdateDto.getUsername())) {
            throw new DatabaseErrorException(USERNAME_TAKEN);
        }
    }
}
