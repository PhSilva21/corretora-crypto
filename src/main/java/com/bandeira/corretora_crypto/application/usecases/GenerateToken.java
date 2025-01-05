package com.bandeira.corretora_crypto.application.usecases;

import com.bandeira.corretora_crypto.application.gateways.TokenGateway;
import com.bandeira.corretora_crypto.infra.persistence.UserEntity;

public class GenerateToken {

    private final TokenGateway tokenGateway;

    public GenerateToken(TokenGateway tokenGateway) {
        this.tokenGateway = tokenGateway;
    }

    public String execute(UserEntity user){
        return tokenGateway.generateToken(user);
    }
}
