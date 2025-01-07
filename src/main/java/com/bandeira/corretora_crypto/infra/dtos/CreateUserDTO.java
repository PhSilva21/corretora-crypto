package com.bandeira.corretora_crypto.infra.dtos;

import com.bandeira.corretora_crypto.domain.enums.UserRole;

public record CreateUserDTO(

        String email,

        String password,

        UserRole role
) {
}
