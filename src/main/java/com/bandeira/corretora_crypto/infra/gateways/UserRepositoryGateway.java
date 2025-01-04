package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.application.gateways.UserGateway;
import com.bandeira.corretora_crypto.domain.User;
import com.bandeira.corretora_crypto.infra.dtos.CreateUserDTO;
import com.bandeira.corretora_crypto.infra.dtos.LoginUserDTO;
import com.bandeira.corretora_crypto.infra.exceptions.EmailAlreadyExists;
import com.bandeira.corretora_crypto.infra.persistence.repository.UserRepository;
import com.bandeira.corretora_crypto.infra.util.UserMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryGateway implements UserGateway{

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final WalletRepositoryGateway walletRepositoryGateway;

    public UserRepositoryGateway(UserRepository userRepository, UserMapper userMapper
            , WalletRepositoryGateway walletRepositoryGateway) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.walletRepositoryGateway = walletRepositoryGateway;
    }

    @Override
    public User findById(Long id) {
        var user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        return userMapper.toUserModel(user);
    }

    @Override
    public void CreateUser(CreateUserDTO request) {
        validateEmail(request.email());

        var user = userMapper.toUserEntity(request.email(), request.password());

        walletRepositoryGateway.CreateWallet(user);

        userRepository.save(user);
    }

    private void validateEmail(String email){
        if(userRepository.findByEmail(email) != null){
            throw new EmailAlreadyExists();
    }
    }
}
