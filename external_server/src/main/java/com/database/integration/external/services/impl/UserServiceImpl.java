package com.database.integration.external.services.impl;

import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.exception.ServiceException;
import com.database.integration.external.services.UserService;
import com.database.integration.core.dto.PasswordInfoDto;
import com.database.integration.external.kafka.producer.KafkaProducer;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import com.database.integration.external.repositories.DepartmentRepository;
import com.database.integration.external.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    KafkaProducer kafkaProducer;

    @Qualifier("userPasswordEncoder")
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().filter(User::isEnabled).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('USER_READ')")
    public User getUser(String username) throws EntityNotInDatabaseException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        Hibernate.initialize(user.getUsername());
        Hibernate.initialize(user.getUserRoles());
        Hibernate.initialize(user.getAuthorities());
        Hibernate.initialize(user.getUserdata());
        return user.isEnabled() ? user : null;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('USER_ROLES_READ')")
    public List<UserRole> getUserRoles(String username) throws EntityNotInDatabaseException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        return new ArrayList<>(user.getUserRoles());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public void deleteUser(String username) throws EntityNotInDatabaseException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        user.setEnabled(false);
        kafkaProducer.send(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('PASSWORD_ADMIN_READ')")
    public PasswordInfoDto getPasswordForAdmin(String username) throws EntityNotInDatabaseException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        return PasswordInfoDto.builder().userVersion(user.getVersion()).username(username).build();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('PASSWORD_ADMIN_UPDATE')")
    public void updatePasswordForAdmin(PasswordInfoDto passwordInfoForAdmin) throws EntityNotInDatabaseException, EntityOptimisticLockException {
        try {
            User user = userRepository.findByUsername(passwordInfoForAdmin.getUsername()).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
            userRepository.detach(user);
            user.setPassword(passwordEncoder.encode(passwordInfoForAdmin.getNewPassword()));
            user.setVersion(passwordInfoForAdmin.getUserVersion());
            userRepository.saveAndFlush(user);
            kafkaProducer.send(user);
        } catch (StaleObjectStateException e) {
            throw new EntityOptimisticLockException(EntityOptimisticLockException.OPTIMISTIC_LOCK);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('PASSWORD_READ')")
    public PasswordInfoDto getPassword(String username) throws EntityNotInDatabaseException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        return PasswordInfoDto.builder().userVersion(user.getVersion()).build();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('PASSWORD_UPDATE')")
    public void updatePassword(PasswordInfoDto passwordInfoDto, String username) throws EntityNotInDatabaseException, ServiceException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotInDatabaseException(EntityNotInDatabaseException.NO_OBJECT));
        if (!(passwordInfoDto.getNewPassword().length() >= 8 && passwordInfoDto.getOldPassword().length() >= 8)) {
            throw new ServiceException(ServiceException.INCORRECT_LENGTH_PASSWORD);
        }
        if (!passwordEncoder.matches(passwordInfoDto.getOldPassword(), user.getPassword())) {
            throw new ServiceException(ServiceException.INCORRECT_PASSWORD);
        }
        if (!passwordEncoder.matches(passwordInfoDto.getNewPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordInfoDto.getNewPassword()));
            user.setVersion(passwordInfoDto.getUserVersion());
            userRepository.saveAndFlush(user);
            kafkaProducer.send(user);
        } else {
            throw new ServiceException(ServiceException.SAME_PASSWORD);
        }

    }

    @Override
    public void mergeUser(User user) {
        userRepository.merge(user);
    }
}
