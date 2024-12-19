package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.application.gateways.CryptoGateway;
import com.bandeira.corretora_crypto.domain.Crypto;
import com.bandeira.corretora_crypto.infra.exceptions.CryptoNotFoundException;
import com.bandeira.corretora_crypto.infra.persistence.repository.CryptoRepository;
import com.bandeira.corretora_crypto.infra.util.CryptoMapper;

import java.math.BigDecimal;
import java.util.List;

public class CryptoRepositoryGateway implements CryptoGateway{

    private final CryptoRepository cryptoRepository;

    private final WalletRepositoryGateway walletRepositoryGateway;

    private final CryptoMapper cryptoMapper;

    public CryptoRepositoryGateway(CryptoRepository cryptoRepository
            , WalletRepositoryGateway walletRepositoryGateway, CryptoMapper cryptoMapper) {
        this.cryptoRepository = cryptoRepository;
        this.walletRepositoryGateway = walletRepositoryGateway;
        this.cryptoMapper = cryptoMapper;
    }

    @Override
        public Crypto findByName(String name) {
        var crypto = cryptoRepository.findByName(name)
                .orElseThrow(CryptoNotFoundException::new);

        return cryptoMapper.toCrypto(crypto);
    }

    @Override
    public List<Crypto> findByPopularity() {
        return List.of();
    }

    @Override
    public List<Crypto> findByRecentLaunch() {
        return List.of();
    }

    public BigDecimal findCryptoPriceByName(String name){
        return findByName(name).getPrice();
    }


}
