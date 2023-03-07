package com.centralserver.repositories;

import com.centralserver.model.users.User;
import com.centralserver.repositories.custom_interface.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, CustomUserRepository {

    @Query("SELECT DISTINCT user FROM User user " +
            "INNER JOIN FETCH user.userRoles AS roles " +
            "WHERE user.username = :username")
    Optional<User> findByUsername(@Param("username") String username);


}
