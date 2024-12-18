package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.application.gateways.WalletGateway;
import com.bandeira.corretora_crypto.domain.Wallet;
import com.bandeira.corretora_crypto.infra.exceptions.WalletNotFound;
import com.bandeira.corretora_crypto.infra.persistence.repository.WalletRepository;
import com.bandeira.corretora_crypto.infra.util.WalletMapper;

public class WalletRepositoryGateway implements WalletGateway{

    private final WalletMapper walletMapper;

    private final WalletRepository walletRepository;

    public WalletRepositoryGateway(WalletMapper walletMapper
            , WalletRepository walletRepository) {
        this.walletMapper = walletMapper;
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet findByWalletById(Long id) {
        var walletEntity = walletRepository.findById(id).orElseThrow(WalletNotFound::new);

        return walletMapper.toWallet(walletEntity);
    }
}
