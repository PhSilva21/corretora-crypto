package com.bandeira.corretora_crypto.infra.util;

import com.bandeira.corretora_crypto.domain.Wallet;
import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import com.bandeira.corretora_crypto.infra.persistence.WalletEntity;

import java.math.BigDecimal;

public class WalletMapper {

    public Wallet toWallet(WalletEntity walletEntity){
        return new Wallet(walletEntity.getId(), walletEntity.getUser(), walletEntity.getBalance()
                , walletEntity.getCurrentValueInvested(),walletEntity.getAssets()
                );
    }

    public WalletEntity newWalletEntity(UserEntity user){
        return new WalletEntity(user);
    }
}
