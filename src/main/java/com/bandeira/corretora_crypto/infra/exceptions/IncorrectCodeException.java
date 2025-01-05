package com.bandeira.corretora_crypto.infra.exceptions;

public class IncorrectCodeException extends RuntimeException{

    public IncorrectCodeException(){
        super("Incorrect code");
    }

    public IncorrectCodeException(String message){
        super(message);
    }
}
