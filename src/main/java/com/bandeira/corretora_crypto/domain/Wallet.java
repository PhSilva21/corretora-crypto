package com.bandeira.corretora_crypto.domain;

import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Wallet {

    private Long id;

    private UserEntity user;

    private BigDecimal balance;

    private BigDecimal currentValueInvested = BigDecimal.ZERO;

    private Map<String, BigDecimal> assets = new HashMap<>();
}
