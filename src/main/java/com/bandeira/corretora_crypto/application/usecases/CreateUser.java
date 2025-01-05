package com.bandeira.corretora_crypto.application.usecases;

import com.bandeira.corretora_crypto.application.gateways.UserGateway;
import com.bandeira.corretora_crypto.infra.dtos.CreateUserDTO;

public class CreateUser {

    private final UserGateway userGateway;

    public CreateUser(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void execute(CreateUserDTO request){
        userGateway.createUser(request);
    }
}
