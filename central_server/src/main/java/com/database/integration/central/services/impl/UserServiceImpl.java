package com.database.integration.central.services.impl;

import static com.database.integration.core.exception.EntityNotInDatabaseException.ErrorMessage.NO_OBJECT;
import static com.database.integration.core.exception.EntityOptimisticLockException.ErrorMessage.OPTIMISTIC_LOCK;

import com.database.integration.central.kafka.producer.KafkaProducer;
import com.database.integration.central.repositories.UserRepository;
import com.database.integration.central.services.UserService;
import com.database.integration.core.dto.PasswordUpdateDto;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.exception.ServiceException;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    KafkaProducer kafkaProducer;

    @Qualifier("userPasswordEncoder")
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
      return users.stream().filter(User::isEnabled).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('USER_READ')")
    public User getUser(String username) throws EntityNotInDatabaseException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
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
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
        return new ArrayList<>(user.getUserRoles());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public void deleteUser(String username) throws EntityNotInDatabaseException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
        user.setEnabled(false);
        kafkaProducer.send(user);
    }

  @Override
  @Transactional
  @PreAuthorize("hasAuthority('PASSWORD_ADMIN_READ')")
  public PasswordUpdateDto getPasswordForAdmin(String username)
      throws EntityNotInDatabaseException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
    return PasswordUpdateDto.builder().userVersion(user.getVersion()).username(username).build();
  }

  @Override
  @Transactional
  @PreAuthorize("hasAuthority('PASSWORD_ADMIN_UPDATE')")
  public void updatePasswordForAdmin(PasswordUpdateDto passwordInfoForAdmin)
      throws EntityNotInDatabaseException, EntityOptimisticLockException {
    try {
      User user = userRepository.findByUsername(passwordInfoForAdmin.getUsername())
          .orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
      userRepository.detach(user);
      user.setPassword(passwordEncoder.encode(passwordInfoForAdmin.getNewPassword()));
      user.setVersion(passwordInfoForAdmin.getUserVersion());
      userRepository.saveAndFlush(user);
      kafkaProducer.send(user);
    } catch (StaleObjectStateException e) {
      throw new EntityOptimisticLockException(OPTIMISTIC_LOCK);
    }
    }

  @Override
  @Transactional
  @PreAuthorize("hasAuthority('PASSWORD_READ')")
  public PasswordUpdateDto getPassword(String username) throws EntityNotInDatabaseException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
    return PasswordUpdateDto.builder().userVersion(user.getVersion()).build();
  }

  @Override
  @Transactional
  @PreAuthorize("hasAuthority('PASSWORD_UPDATE')")
  public void updatePassword(PasswordUpdateDto passwordUpdateDto, String username)
      throws EntityNotInDatabaseException, ServiceException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotInDatabaseException(NO_OBJECT));
    if (!(
        passwordUpdateDto.getNewPassword().length() >= 8
            && passwordUpdateDto.getOldPassword().length() >= 8)) {
      throw new ServiceException(ServiceException.INCORRECT_LENGTH_PASSWORD);
    }
    if (!passwordEncoder.matches(passwordUpdateDto.getOldPassword(), user.getPassword())) {
      throw new ServiceException(ServiceException.INCORRECT_PASSWORD);
    }
    if (!passwordEncoder.matches(passwordUpdateDto.getNewPassword(), user.getPassword())) {
      user.setPassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
      user.setVersion(passwordUpdateDto.getUserVersion());
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
