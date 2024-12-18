package com.bandeira.corretora_crypto.infra.exceptions;

public class WalletNotFound extends RuntimeException{

    public WalletNotFound(){
        super("Wallet not found");
    }

    public WalletNotFound(String message){
        super(message);
    }
}


