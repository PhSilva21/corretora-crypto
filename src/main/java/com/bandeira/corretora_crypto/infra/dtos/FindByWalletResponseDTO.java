package com.bandeira.corretora_crypto.infra.dtos;

import java.math.BigDecimal;
import java.util.List;

public record FindByWalletResponseDTO(

        BigDecimal totalInvested,

        BigDecimal currentTotalValue,

        BigDecimal profitOrLoss,

        List<AssetDTO> assets
) {
}
