package com.bandeira.corretora_crypto.infra.exceptions;

public class EmailIsNotYetRegisteredException extends RuntimeException {


    public EmailIsNotYetRegisteredException(){
      super("This email is not yet registered");
    }

    public EmailIsNotYetRegisteredException(String message) {
        super(message);
    }
}
