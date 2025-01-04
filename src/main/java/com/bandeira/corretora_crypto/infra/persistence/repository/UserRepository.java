package com.bandeira.corretora_crypto.infra.persistence.repository;

import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserDetails findByEmail(String email);
}
