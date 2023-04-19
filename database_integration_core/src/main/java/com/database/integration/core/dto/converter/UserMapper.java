package com.database.integration.core.dto.converter;

import com.database.integration.core.dto.UserRegistrationDto;
import com.database.integration.core.dto.UserUpdateDto;
import com.database.integration.core.model.Address;
import com.database.integration.core.model.User;
import com.database.integration.core.model.UserRole;
import com.database.integration.core.model.Userdata;
import java.util.Calendar;
import java.util.Set;

public class UserMapper {

  public static User toUser(UserRegistrationDto data, String password, Set<UserRole> roles) {
    return User.builder()
        .username(data.getUsername())
        .password(password)
        .enabled(true)
        .userRoles(roles)
        .userdata(toUserdata(data))
        .build();
  }

  private static Userdata toUserdata(UserRegistrationDto data) {
    return Userdata.builder()
        .email(data.getEmail())
        .surname(data.getSurname())
        .position(data.getPosition())
        .name(data.getName())
        .workplace(data.getWorkplace())
        .dateOfJoin(Calendar.getInstance())
        .address(toAddress(data)).build();
  }

  private static Address toAddress(UserRegistrationDto data) {
    return Address.builder()
        .flatNumber(data.getFlatNumber())
        .buildingNumber(data.getHouseNumber())
        .street(data.getStreet())
        .city(data.getCity())
        .build();
  }


  public static User toUser(UserUpdateDto userUpdateDto, User user) {
    user.setUsername(userUpdateDto.getUsername());
    user.setVersion(userUpdateDto.getVersionUser());
    user.setUserdata(toUserdata(userUpdateDto, user.getUserdata()));
    return user;
  }

  private static Userdata toUserdata(UserUpdateDto userUpdateDto, Userdata userdata) {
    userdata.setEmail(userUpdateDto.getEmail());
    userdata.setSurname(userUpdateDto.getSurname());
    userdata.setPosition(userUpdateDto.getPosition());
    userdata.setName(userUpdateDto.getName());
    userdata.setWorkplace(userUpdateDto.getWorkplace());
    userdata.setDateOfJoin(Calendar.getInstance());
    userdata.setAddress(toAddress(userUpdateDto, userdata.getAddress()));
    userdata.setVersion(userUpdateDto.getVersionUserdata());
    return userdata;
  }

  private static Address toAddress(UserUpdateDto userUpdateDto, Address address) {
    address.setFlatNumber(userUpdateDto.getFlatNumber());
    address.setBuildingNumber(userUpdateDto.getHouseNumber());
    address.setStreet(userUpdateDto.getStreet());
    address.setCity(userUpdateDto.getCity());
    return address;
  }
}
