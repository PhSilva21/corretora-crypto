package com.bandeira.corretora_crypto.application.gateways;

import com.bandeira.corretora_crypto.infra.persistence.UserEntity;

public interface TokenGateway {

    String generateToken(UserEntity user);

    String validateToken(String token);

}
