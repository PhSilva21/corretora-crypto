package com.bandeira.corretora_crypto.infra.persistence.repository;

import com.bandeira.corretora_crypto.infra.persistence.CryptoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CryptoRepository extends JpaRepository<CryptoEntity, Long> {

    Optional<CryptoEntity> findByName(String name);
}
