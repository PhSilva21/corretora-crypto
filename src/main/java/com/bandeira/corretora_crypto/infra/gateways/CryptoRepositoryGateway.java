package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.application.gateways.CryptoGateway;
import com.bandeira.corretora_crypto.domain.Crypto;
import com.bandeira.corretora_crypto.infra.exceptions.CryptoNotFoundException;
import com.bandeira.corretora_crypto.infra.persistence.repository.CryptoRepository;
import com.bandeira.corretora_crypto.infra.util.CryptoMapper;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        return findAll().stream()
                .sorted(Comparator.comparingInt(Crypto::getPopularity).reversed())
                .limit(10)
                .toList();
    }

    public List<Crypto> findAll(){
        return cryptoRepository.findAll().stream()
                .map(c -> new Crypto(
                        c.getId(),
                        c.getSymbol(),
                        c.getName(),
                        c.getPrice(),
                        c.getPopularity(),
                        c.getLaunchDate(),
                        c.getTransactions()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Crypto> findByRecentLaunch() {
        return findAll().stream()
                .sorted(Comparator.comparing(Crypto::getLaunchDate).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public BigDecimal findCryptoPriceByName(String name){
        return findByName(name).getPrice();
    }

    public Crypto findCryptoByNameToBuy(String name) {
        var crypto = cryptoRepository.findByName(name)
                .orElseThrow(CryptoNotFoundException::new);

        crypto.incrementPopularity();

        cryptoRepository.save(crypto);

        return cryptoMapper.toCrypto(crypto);
    }
}
