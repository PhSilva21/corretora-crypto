package com.bandeira.corretora_crypto.application.gateways;

import com.bandeira.corretora_crypto.domain.Wallet;

public interface WalletGateway {

    Wallet findByWalletById(Long id);
}
