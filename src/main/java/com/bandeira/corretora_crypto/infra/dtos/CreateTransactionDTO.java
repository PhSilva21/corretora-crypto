package com.bandeira.corretora_crypto.infra.dtos;

import com.bandeira.corretora_crypto.domain.enums.TransactionType;
import com.bandeira.corretora_crypto.infra.persistence.CryptoEntity;
import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateTransactionDTO(

        UserEntity user,

        CryptoEntity crypto,

        BigDecimal quantity,

        BigDecimal priceAtTransaction,

        LocalDateTime date,

        TransactionType transactionType

        ) {
}
