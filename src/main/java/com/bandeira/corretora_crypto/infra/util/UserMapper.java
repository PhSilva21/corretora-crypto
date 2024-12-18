package com.bandeira.corretora_crypto.infra.util;


import com.bandeira.corretora_crypto.domain.User;
import com.bandeira.corretora_crypto.infra.persistence.UserEntity;

public class UserMapper {

    public User toUserModel(UserEntity user){
        return new User(user.getId(), user.getEmail(), user.getWallet());
    }
}
