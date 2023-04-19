package com.database.integration.central.services.impl;

import com.database.integration.central.kafka.producer.KafkaProducer;
import com.database.integration.central.repositories.UserRepository;
import com.database.integration.central.repositories.UserRoleRepository;
import com.database.integration.central.repositories.UserdataRepository;
import com.database.integration.central.services.AccountUpdateService;
import com.database.integration.core.dto.AccountDto;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.exception.EntityNotInDatabaseException;
import com.database.integration.core.exception.EntityOptimisticLockException;
import com.database.integration.core.model.Address;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import com.database.integration.core.model.Userdata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.EMAIL_TAKEN;
import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.USERNAME_TAKEN;
import static com.database.integration.core.exception.EntityNotInDatabaseException.ErrorMessage.NO_OBJECT;
import static com.database.integration.core.exception.EntityOptimisticLockException.ErrorMessage.OPTIMISTIC_LOCK;

@Service
public class AccountUpdateServiceImpl implements AccountUpdateService {

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
  public void updateAccountByAdmin(AccountDto accountDto, List<String> roles)
      throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException {
    User user = getUser(accountDto);
    Address address = getAddress(accountDto);
    Userdata userdata = getUserdata(accountDto, user, address);

    if (roles != null) {
      List<UserRole> userRoles = getUserRoles(roles);
      user.setUserRoles(userRoles);
    }

    saveAndFlushAccount(user, userdata);
  }

  private List<UserRole> getUserRoles(List<String> roles) {
    return userRoleRepository.findAll().stream()
        .filter(role -> roles.stream().anyMatch(name -> name.equals(role.getName())))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  @PreAuthorize("hasAuthority('ACCOUNT_UPDATE_SELF')")
  public void updateAccountByUser(AccountDto accountDto)
      throws EntityNotInDatabaseException, EntityOptimisticLockException, DatabaseErrorException {
    User user = getUser(accountDto);
    Address address = getAddress(accountDto);
    Userdata userdata = getUserdata(accountDto, user, address);
    saveAndFlushAccount(user, userdata);
  }

  private User getUser(AccountDto accountDto)
      throws EntityNotInDatabaseException, DatabaseErrorException {
      User user = userRepository.findById(accountDto.getId()).orElseThrow(
              (() -> new EntityNotInDatabaseException(NO_OBJECT)));
    usernameIsPresent(accountDto.getUsername(), user.getUsername());
    userRepository.detach(user);
    user.setUsername(accountDto.getUsername());
    user.setAccountLocked(false);
    user.setCredentialsExpired(false);
    user.setVersion(accountDto.getVersionUser());
    return user;
  }

  private void usernameIsPresent(String newUsername, String oldUsername)
      throws DatabaseErrorException {
    if (userRepository.findByUsername(newUsername).isPresent() && !oldUsername.equals(
        newUsername)) {
        throw new DatabaseErrorException(USERNAME_TAKEN);
    }
  }

  private Userdata getUserdata(AccountDto accountDto, User user, Address address)
      throws EntityNotInDatabaseException, DatabaseErrorException {
      Userdata userdata = userdataRepository.findById(user.getUserdata().getId()).orElseThrow(
              () -> new EntityNotInDatabaseException(NO_OBJECT));
    userdataRepository.detach(userdata);
    emailIsPresent(accountDto.getEmail(), userdata.getEmail());
    userdata.setEmail(accountDto.getEmail());
    userdata.setSurname(accountDto.getSurname());
    userdata.setPosition(accountDto.getPosition());
    userdata.setName(accountDto.getName());
    userdata.setWorkplace(accountDto.getWorkplace());
    userdata.setDateOfJoin(Calendar.getInstance());
    userdata.setAddress(address);
    userdata.setVersion(accountDto.getVersionUserdata());
    return userdata;
  }

  private void emailIsPresent(String newEmail, String oldEmail) throws DatabaseErrorException {
    if (userdataRepository.findByEmail(newEmail).isPresent() && !oldEmail
        .equals(newEmail)) {
        throw new DatabaseErrorException(EMAIL_TAKEN);
    }
  }
  private static Address getAddress(AccountDto accountDto) {
    Address address = new Address();
    address.setFlatNumber(accountDto.getFlatNumber());
    address.setBuildingNumber(accountDto.getHouseNumber());
    address.setStreet(accountDto.getStreet());
    address.setCity(accountDto.getCity());
    return address;
  }

  private void saveAndFlushAccount(User user, Userdata userdata)
      throws EntityOptimisticLockException {
    try {
      user.setUserdata(userdata);
      User savedUser = userRepository.saveAndFlush(user);
      kafkaProducer.send(savedUser);
    } catch (ObjectOptimisticLockingFailureException e) {
        throw new EntityOptimisticLockException(OPTIMISTIC_LOCK);
    }
  }
}
