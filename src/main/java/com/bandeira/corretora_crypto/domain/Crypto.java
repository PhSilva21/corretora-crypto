package com.bandeira.corretora_crypto.domain;

import com.bandeira.corretora_crypto.infra.persistence.TransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Crypto {

    private Long id;

    private String symbol;

    private String name;

    private BigDecimal price;

    private Integer popularity;

    private LocalDate launchDate;

    private List<TransactionEntity> transactions = new ArrayList<>();
}
