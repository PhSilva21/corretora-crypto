package com.bandeira.corretora_crypto.application.usecases;

import com.bandeira.corretora_crypto.application.gateways.CryptoGateway;
import com.bandeira.corretora_crypto.domain.Crypto;

import java.util.List;

public class FindByRecentLaunch {

    private final CryptoGateway cryptoGateway;

    public FindByRecentLaunch(CryptoGateway cryptoGateway) {
        this.cryptoGateway = cryptoGateway;
    }

    public List<Crypto> execute(){
        return cryptoGateway.findByRecentLaunch();
    }
}
