package com.database.integration.central.services.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.database.integration.central.repositories.UserRepository;
import com.database.integration.core.model.User;
import com.database.integration.core.model.Userdata;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

  @InjectMocks
  UserDetailsServiceImpl userDetailsService;

  @Mock
  UserRepository userRepository;


  @Test
  void shouldLoadUserByUsername() {
    //given
    User user = getUser();
    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    //when
    UserDetails result = userDetailsService.loadUserByUsername(user.getUsername());
    //then
    assertThat(result).isEqualTo(user);
  }

  private User getUser() {
    return User.builder()
        .id(UUID.randomUUID())
        .username("username")
        .password("password")
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .userRoles(Collections.emptySet())
        .userdata(new Userdata())
        .enabled(true)
        .version(0)
        .build();
  }
}