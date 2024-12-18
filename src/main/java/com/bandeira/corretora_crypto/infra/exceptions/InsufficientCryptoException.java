package com.bandeira.corretora_crypto.infra.exceptions;

public class InsufficientCryptoException extends RuntimeException {

    public InsufficientCryptoException(){
        super("Insufficient crypto");
    }

    public InsufficientCryptoException(String message) {
        super(message);
    }
}
