package com.bandeira.corretora_crypto.infra.util;

import com.bandeira.corretora_crypto.domain.Wallet;
import com.bandeira.corretora_crypto.infra.persistence.WalletEntity;

public class WalletMapper {

    public Wallet toWallet(WalletEntity walletEntity){
        return new Wallet(walletEntity.getId(), walletEntity.getUser(), walletEntity.getBalance()
                , walletEntity.getTotalInvested(),walletEntity.getAssets()
                );
    }
}
