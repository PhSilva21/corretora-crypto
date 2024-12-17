package com.bandeira.corretora_crypto.domain;

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

    private WalletEntity wallet;
}
