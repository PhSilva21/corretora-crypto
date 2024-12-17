package com.bandeira.corretora_crypto.application.usecases;

import com.bandeira.corretora_crypto.application.gateways.WalletGateway;
import com.bandeira.corretora_crypto.domain.Wallet;

public class FindByWalletById {

    private final WalletGateway walletGateway;

    public FindByWalletById(WalletGateway walletGateway) {
        this.walletGateway = walletGateway;
    }

    private Wallet execute(Long id){
        return walletGateway.findByWalletById(id);
    }
}
