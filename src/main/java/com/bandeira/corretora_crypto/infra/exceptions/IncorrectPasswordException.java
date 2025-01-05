package com.bandeira.corretora_crypto.infra.exceptions;

public class IncorrectPasswordException extends RuntimeException{

    public IncorrectPasswordException(){
        super("Incorrect password");
    }

    public IncorrectPasswordException(String message){
        super(message);
    }
}
