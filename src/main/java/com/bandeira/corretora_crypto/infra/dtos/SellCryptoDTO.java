package com.bandeira.corretora_crypto.infra.dtos;

import java.math.BigDecimal;

public record SellCryptoDTO(

        Long userId,

        String cryptoName,

        BigDecimal quantity
) {
}
