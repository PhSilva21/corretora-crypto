package com.bandeira.corretora_crypto.infra.persistence;

import jakarta.persistence.*;
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
@Entity
@Table(name = "tb_wallets")
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

    @Column(nullable = true)
    private BigDecimal balance;

    @Column
    private BigDecimal currentValueInvested = BigDecimal.ZERO;

    @ElementCollection
    @CollectionTable(name = "wallet", joinColumns = @JoinColumn(name = "wallet_id"))
    @MapKeyColumn(name = "crypto_name")
    @Column(name = "quantity")
    private Map<String, BigDecimal> assets = new HashMap<>();


    public void addInvestment(BigDecimal amount) {
        this.currentValueInvested = this.currentValueInvested.add(amount);
    }

    public void subtractInvestment(BigDecimal amount) {
        this.currentValueInvested = this.currentValueInvested.subtract(amount);
    }

    public WalletEntity(UserEntity user) {
        this.user = user;

    }
}
