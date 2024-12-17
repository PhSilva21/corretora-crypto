package com.bandeira.corretora_crypto.application.usecases;

import com.bandeira.corretora_crypto.application.gateways.CryptoGateway;
import com.bandeira.corretora_crypto.domain.Crypto;

import java.util.List;

public class FindByPopularity {

    private final CryptoGateway cryptoGateway;

    public FindByPopularity(CryptoGateway cryptoGateway) {
        this.cryptoGateway = cryptoGateway;
    }

    public List<Crypto> execute(){
        return cryptoGateway.findByPopularity();
    }
}
