package com.bandeira.corretora_crypto.application.usecases;

import com.bandeira.corretora_crypto.application.gateways.CryptoGateway;
import com.bandeira.corretora_crypto.domain.Crypto;

import java.util.Optional;

public class FindByName {

    private final CryptoGateway cryptoGateway;

    public FindByName(CryptoGateway cryptoGateway) {
        this.cryptoGateway = cryptoGateway;
    }

    public Optional<Crypto> execute(String name){
        return cryptoGateway.findByName(name);
    }
}
