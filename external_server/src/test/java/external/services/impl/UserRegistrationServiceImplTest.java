package external.services.impl;

import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.EMAIL_TAKEN;
import static com.database.integration.core.exception.DatabaseErrorException.ErrorMessage.USERNAME_TAKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.database.integration.core.dto.UserRegistrationDto;
import com.database.integration.core.dto.converter.UserMapper;
import com.database.integration.core.exception.DatabaseErrorException;
import com.database.integration.core.model.Authority;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import com.database.integration.core.model.Userdata;
import com.database.integration.external.kafka.producer.KafkaProducer;
import com.database.integration.external.repositories.UserRepository;
import com.database.integration.external.repositories.UserRoleRepository;
import com.database.integration.external.repositories.UserdataRepository;
import com.database.integration.external.services.impl.UserRegistrationServiceImpl;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTest {

  @InjectMocks
  UserRegistrationServiceImpl accountRegistrationService;

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

  User user;
  UserRegistrationDto userRegistrationDto;

  @BeforeEach
  void setup() {
    userRegistrationDto = prepareRegistrationDTO();
    var encodedPassword = Base64.getEncoder()
        .encodeToString(userRegistrationDto.getPassword().getBytes());
    var authorities = Set.of(new Authority(UUID.randomUUID(), "READ_DEPARTMENT", true));
    var userRole = new UserRole(UUID.randomUUID(), "user", true, authorities);
    user = UserMapper.toUser(userRegistrationDto, encodedPassword, Set.of(userRole));
    user.setId(UUID.randomUUID());
  }

  @Test
  @Tag("New user registration test")
  void shouldRegisterNewUser() throws DatabaseErrorException {
    //given
    when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn(user.getPassword());
    when(userRoleRepository.findAll()).thenReturn(user.getUserRoles().stream().toList());
    when(userRepository.findByUsername(userRegistrationDto.getUsername())).thenReturn(
        Optional.empty());
    when(userdataRepository.findByEmail(userRegistrationDto.getEmail())).thenReturn(
        Optional.empty());
    when(userRepository.saveAndFlush(any())).thenReturn(user);
    //when
    User result = accountRegistrationService.registerNewUserAccount(userRegistrationDto, true);
    //then
    verify(kafkaProducer, times(1)).send(result);
    assertThat(result).isEqualTo(user);
  }

  @Test
  void shouldThrowExceptionDuringCreateWhenEmailWasTaken() {
    //given
    when(userdataRepository.findByEmail(userRegistrationDto.getEmail())).thenReturn(
        Optional.of(new Userdata()));
    //when
    Assertions.assertThatExceptionOfType(DatabaseErrorException.class)
        .isThrownBy(
            () -> accountRegistrationService.registerNewUserAccount(userRegistrationDto, true))
        .withMessage(EMAIL_TAKEN.message);
  }

  @Test
  void shouldThrowExceptionDuringCreateWhenUsernameWasTaken() {
    //given
    when(userRepository.findByUsername(userRegistrationDto.getUsername())).thenReturn(
        Optional.of(new User()));
    //when
    Assertions.assertThatExceptionOfType(DatabaseErrorException.class)
        .isThrownBy(
            () -> accountRegistrationService.registerNewUserAccount(userRegistrationDto, true))
        .withMessage(USERNAME_TAKEN.message);
  }


  private UserRegistrationDto prepareRegistrationDTO() {
    return UserRegistrationDto.builder()
        .username("username")
        .password("password")
        .email("user@user.com")
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