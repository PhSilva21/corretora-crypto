package com.bandeira.corretora_crypto.infra.util;


import com.bandeira.corretora_crypto.domain.Crypto;
import com.bandeira.corretora_crypto.infra.persistence.CryptoEntity;

public class CryptoMapper {


    public Crypto toCrypto(CryptoEntity crypto){
        return new Crypto(crypto.getId(), crypto.getSymbol(), crypto.getName(), crypto.getPrice()
                , crypto.getPopularity(), crypto.getLaunchDate(), crypto.getTransactions());
    }


}
