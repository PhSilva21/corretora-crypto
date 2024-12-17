package com.bandeira.corretora_crypto.infra.persistence.repository;

import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
