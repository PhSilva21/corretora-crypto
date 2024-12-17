package com.bandeira.corretora_crypto.application.usecases;

import com.bandeira.corretora_crypto.application.gateways.UserGateway;
import com.bandeira.corretora_crypto.domain.User;

public class FindByUserById {

    private final UserGateway userGateway;

    public FindByUserById(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    User execute(Long id){
        return userGateway.findById(id);
    }
}
