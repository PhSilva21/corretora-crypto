package com.bandeira.corretora_crypto.infra.dtos;

import java.math.BigDecimal;

public record AssetDTO(

        String cryptoName,

        BigDecimal quantity,

        BigDecimal averagePrice,

        BigDecimal currentPrice,

        BigDecimal currentValue
) {
}
