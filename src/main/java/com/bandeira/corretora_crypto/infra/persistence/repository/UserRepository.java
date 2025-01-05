package com.bandeira.corretora_crypto.infra.persistence.repository;

import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserDetails> findByEmail(String email);
}
