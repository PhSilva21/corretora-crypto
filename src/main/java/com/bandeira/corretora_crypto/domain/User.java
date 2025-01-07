package com.bandeira.corretora_crypto.domain;

import com.bandeira.corretora_crypto.domain.enums.UserRole;
import com.bandeira.corretora_crypto.infra.persistence.WalletEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private Long id;

    private String email;

    private String password;

    private WalletEntity wallet;

    private UserRole userRole;

    public User(String email, String password, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
}
