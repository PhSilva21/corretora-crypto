package com.bandeira.corretora_crypto.infra.exceptions;

public class CryptoNotFoundException extends RuntimeException{

    public CryptoNotFoundException(){
        super("Crypto not found");
    }

    public CryptoNotFoundException(String message){
        super(message);
    }
}
