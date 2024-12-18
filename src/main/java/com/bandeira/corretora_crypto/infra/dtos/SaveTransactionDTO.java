package com.bandeira.corretora_crypto.infra.dtos;

import com.bandeira.corretora_crypto.domain.Crypto;
import com.bandeira.corretora_crypto.domain.User;

import java.math.BigDecimal;

public record SaveTransactionDTO(

        User user,

        Crypto crypto,

        BigDecimal quantity,

        BigDecimal total
) {
}