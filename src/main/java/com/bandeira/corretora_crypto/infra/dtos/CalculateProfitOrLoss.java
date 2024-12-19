package com.bandeira.corretora_crypto.infra.dtos;

import java.math.BigDecimal;

public record CalculateProfitOrLoss(

        BigDecimal currentValue,

        BigDecimal totalValue
        ) {
}
