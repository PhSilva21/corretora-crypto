package com.bandeira.corretora_crypto.application.usecases;

import com.bandeira.corretora_crypto.application.gateways.TransactionGateway;
import com.bandeira.corretora_crypto.infra.dtos.BuyCryptoDTO;

public class BuyCrypto {

    private final TransactionGateway transactionGateway;

    public BuyCrypto(TransactionGateway transactionGateway) {
        this.transactionGateway = transactionGateway;
    }

    public void execute(BuyCryptoDTO request){
        transactionGateway.buyCrypto(request);
    }
}
