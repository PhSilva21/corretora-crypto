package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.application.gateways.UserGateway;
import com.bandeira.corretora_crypto.domain.User;
import com.bandeira.corretora_crypto.infra.persistence.repository.UserRepository;
import com.bandeira.corretora_crypto.infra.util.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryGateway implements UserGateway{

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserRepositoryGateway(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User findById(Long id) {
        var user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        return userMapper.toUserModel(user);
    }
}
