package com.bandeira.corretora_crypto.application.gateways;

import com.bandeira.corretora_crypto.domain.User;
import com.bandeira.corretora_crypto.infra.dtos.CreateUserDTO;
import com.bandeira.corretora_crypto.infra.dtos.LoginUserDTO;

public interface UserGateway {

    User findById(Long id);
    
    void CreateUser(CreateUserDTO request);

}
