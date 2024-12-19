package com.bandeira.corretora_crypto.infra.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tb_cryptos")
public class CryptoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private BigDecimal price;

    @Column
    private int popularity;

    @Column
    private LocalDate launchDate;

    @JsonIgnore
    @OneToMany(mappedBy = "crypto")
    private List<TransactionEntity> transactions = new ArrayList<>();

    public void incrementPopularity() {
        this.popularity++;
    }
}
