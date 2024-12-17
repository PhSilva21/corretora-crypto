package com.bandeira.corretora_crypto.application.usecases;

import com.bandeira.corretora_crypto.application.gateways.TransactionGateway;
import com.bandeira.corretora_crypto.infra.dtos.SellCryptoDTO;

public class SellCrypto {

    private final TransactionGateway transactionGateway;

    public SellCrypto(TransactionGateway transactionGateway) {
        this.transactionGateway = transactionGateway;
    }

    public void execute(SellCryptoDTO request){
        transactionGateway.sellCrypto(request);
    }
}
