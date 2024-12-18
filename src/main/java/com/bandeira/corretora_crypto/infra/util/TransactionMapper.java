package com.bandeira.corretora_crypto.infra.util;

import com.bandeira.corretora_crypto.infra.dtos.CreateTransactionDTO;
import com.bandeira.corretora_crypto.infra.persistence.TransactionEntity;

public class TransactionMapper {

    public TransactionEntity toTransactionEntity(CreateTransactionDTO request){
        return new TransactionEntity(request.user(), request.crypto(), request.quantity()
                , request.priceAtTransaction(), request.date(), request.transactionType());
    }
}
