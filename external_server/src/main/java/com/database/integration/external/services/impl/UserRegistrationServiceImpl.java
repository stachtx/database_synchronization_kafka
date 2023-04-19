package com.database.integration.external.services.impl;

import static com.database.integration.core.dto.converter.UserMapper.toUser;
import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.EMAIL_TAKEN;
import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.USERNAME_TAKEN;

import com.database.integration.core.dto.UserRegistrationDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import com.database.integration.external.kafka.producer.KafkaProducer;
import com.database.integration.external.repositories.UserRepository;
import com.database.integration.external.repositories.UserRoleRepository;
import com.database.integration.external.repositories.UserdataRepository;
import com.database.integration.external.services.UserRegistrationService;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {


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
  @Transactional(isolation = Isolation.READ_COMMITTED)
  @PreAuthorize("hasAuthority('USER_CREATE')")
  public User registerNewUserAccount(final UserRegistrationDto userRegistrationDto,
      boolean verified)
      throws DatabaseErrorException {
    validation(userRegistrationDto);
    String encodedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
    var userRoles = getUserRoles(userRegistrationDto);
    User createdUser = userRepository.saveAndFlush(
        toUser(userRegistrationDto, encodedPassword, userRoles));
    kafkaProducer.send(createdUser);
    return createdUser;
  }

  private Set<UserRole> getUserRoles(UserRegistrationDto data) {
    if (data.getRoles() != null && !data.getRoles().isEmpty()) {
      return userRoleRepository.findAll().stream()
          .filter(role -> data.getRoles().stream().anyMatch(name -> name.equals(role.getName())))
          .collect(
              Collectors.toSet());
    }
    return Collections.emptySet();
  }

  private void validation(UserRegistrationDto data) throws DatabaseErrorException {
    if (userdataRepository.findByEmail(data.getEmail()).isPresent()) {
      throw new DatabaseErrorException(EMAIL_TAKEN);
    }
    if (userRepository.findByUsername(data.getUsername()).isPresent()) {
      throw new DatabaseErrorException(USERNAME_TAKEN);
    }
  }
}
