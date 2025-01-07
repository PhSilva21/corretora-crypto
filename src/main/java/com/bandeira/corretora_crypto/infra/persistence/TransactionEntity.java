package com.bandeira.corretora_crypto.infra.persistence;


import com.bandeira.corretora_crypto.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "crypto_id", nullable = false)
    private CryptoEntity crypto;

    private BigDecimal quantity;

    private BigDecimal priceAtTransaction;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;


    public TransactionEntity(UserEntity user, CryptoEntity crypto, BigDecimal quantity
            , BigDecimal priceAtTransaction, LocalDateTime date, TransactionType transactionType) {
        this.user = user;
        this.crypto = crypto;
        this.quantity = quantity;
        this.priceAtTransaction = priceAtTransaction;
        this.date = date;
        this.transactionType = transactionType;
    }
}
