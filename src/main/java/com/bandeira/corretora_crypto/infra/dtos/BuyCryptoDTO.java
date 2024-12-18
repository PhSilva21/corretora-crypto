package com.bandeira.corretora_crypto.infra.dtos;

import java.math.BigDecimal;

public record BuyCryptoDTO(

        Long userId,

        String cryptoName,

        BigDecimal quantity
) {
}
