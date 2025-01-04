package com.bandeira.corretora_crypto.infra.exceptions;

public class EmailAlreadyExists extends RuntimeException {


    public EmailAlreadyExists(){
      super("Email already exists");
    }

    public EmailAlreadyExists(String message) {
        super(message);
    }
}
