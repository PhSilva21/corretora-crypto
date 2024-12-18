package com.bandeira.corretora_crypto.infra.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(){
        super("User not found");
    }

    public UserNotFoundException(String message){
        super(message);
    }
}
