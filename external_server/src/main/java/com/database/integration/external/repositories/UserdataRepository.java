package com.database.integration.external.repositories;

import com.database.integration.core.model.Userdata;
import com.database.integration.external.repositories.custom_interface.CustomUserdataRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserdataRepository extends JpaRepository<Userdata, UUID>, CustomUserdataRepository {


    @Query("SELECT DISTINCT user FROM Userdata user " +
            "WHERE user.email = :email")
    Optional<Userdata> findByEmail(@Param("email") String email);
}
