package com.bandeira.corretora_crypto.infra.dtos;

import com.bandeira.corretora_crypto.domain.Crypto;
import com.bandeira.corretora_crypto.domain.User;
import com.bandeira.corretora_crypto.domain.enums.TransactionType;

import java.math.BigDecimal;

public record UpdateWalletDTO(

    User user,

    Crypto crypto,

    BigDecimal totalPrice,

    BigDecimal quantityCrypto,

    TransactionType transactionType
){
}
