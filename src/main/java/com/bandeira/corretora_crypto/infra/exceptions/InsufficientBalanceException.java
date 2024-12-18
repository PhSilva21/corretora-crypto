package com.bandeira.corretora_crypto.infra.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(){
        super("Insufficient balance");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
