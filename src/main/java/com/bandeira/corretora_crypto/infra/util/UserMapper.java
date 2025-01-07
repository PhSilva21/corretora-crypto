package com.bandeira.corretora_crypto.infra.util;


import com.bandeira.corretora_crypto.domain.User;
import com.bandeira.corretora_crypto.domain.enums.UserRole;
import com.bandeira.corretora_crypto.infra.dtos.CreateUserDTO;
import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import com.bandeira.corretora_crypto.infra.persistence.WalletEntity;

public class UserMapper {

    public User toUserModel(UserEntity user){
        return new User(user.getId(), user.getEmail(), user.getWallet());
    }

    public UserEntity toUserEntity(String email, String password, UserRole role){
        return new UserEntity(email, password, role);
    }
}
