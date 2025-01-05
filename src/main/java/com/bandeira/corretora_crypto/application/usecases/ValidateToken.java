package com.bandeira.corretora_crypto.application.usecases;

import com.bandeira.corretora_crypto.application.gateways.TokenGateway;
import com.bandeira.corretora_crypto.infra.persistence.UserEntity;

public class ValidateToken {

    private final TokenGateway tokenGateway;

    public ValidateToken(TokenGateway tokenGateway) {
        this.tokenGateway = tokenGateway;
    }

    public String execute(String token){
        return tokenGateway.validateToken(token);
    }
}
