package com.bandeira.corretora_crypto.infra.persistence.repository;

import com.bandeira.corretora_crypto.infra.persistence.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
