package com.bandeira.corretora_crypto.application.gateways;

import com.bandeira.corretora_crypto.domain.User;

public interface UserGateway {

    User findById(Long id);
}
