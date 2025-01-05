package com.bandeira.corretora_crypto.application.gateways;

import com.bandeira.corretora_crypto.domain.User;
import com.bandeira.corretora_crypto.infra.dtos.CreateUserDTO;
import com.bandeira.corretora_crypto.infra.dtos.LoginUserDTO;
import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface UserGateway {

    User findById(Long id);
    
    void createUser(CreateUserDTO request);

    void login(LoginUserDTO request);

    void confirmationEmail(String code, UserEntity user)
            throws MessagingException, UnsupportedEncodingException;
}
