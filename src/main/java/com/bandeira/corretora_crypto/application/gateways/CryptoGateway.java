package com.bandeira.corretora_crypto.application.gateways;


import com.bandeira.corretora_crypto.domain.Crypto;

import java.util.List;

public interface CryptoGateway {

    Crypto findByName(String name);

    List<Crypto> findByPopularity();

    List<Crypto> findByRecentLaunch();


}
