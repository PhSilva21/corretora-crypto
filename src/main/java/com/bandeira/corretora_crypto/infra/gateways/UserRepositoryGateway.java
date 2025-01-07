package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.application.gateways.UserGateway;
import com.bandeira.corretora_crypto.domain.User;
import com.bandeira.corretora_crypto.infra.dtos.CreateUserDTO;
import com.bandeira.corretora_crypto.infra.dtos.LoginUserDTO;
import com.bandeira.corretora_crypto.infra.exceptions.*;
import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import com.bandeira.corretora_crypto.infra.persistence.repository.UserRepository;
import com.bandeira.corretora_crypto.infra.util.UserMapper;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class UserRepositoryGateway implements UserGateway{

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final WalletRepositoryGateway walletRepositoryGateway;

    private final PasswordEncoder passwordEncoder;

    private final SendingRepositoryGateway sendingRepositoryGateway;

    public UserRepositoryGateway(UserRepository userRepository, UserMapper userMapper
            , WalletRepositoryGateway walletRepositoryGateway, PasswordEncoder passwordEncoder, SendingRepositoryGateway sendingRepositoryGateway) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.walletRepositoryGateway = walletRepositoryGateway;
        this.passwordEncoder = passwordEncoder;
        this.sendingRepositoryGateway = sendingRepositoryGateway;
    }

    @Override
    public User findById(Long id) {
        var user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        return userMapper.toUserModel(user);
    }

    @Override
    public void createUser(CreateUserDTO request) {
        validateEmail(request.email());

        var user = userMapper.toUserEntity(request.email(), request.password(), request.role());

        walletRepositoryGateway.CreateWallet(user);

        userRepository.save(user);
    }

    @Override
    public void login(LoginUserDTO request){
        var user = findByEmail(request.email());

        validatePassword(request.password(), user.getPassword());
    }

    private void validateEmail(String email){
        if(userRepository.findByEmail(email).isPresent()){
            throw new EmailAlreadyExists();
        }
    }

    @Override
    public void confirmationEmail(String code, UserEntity user)
            throws MessagingException, UnsupportedEncodingException {

        var confirmationCode = sendingRepositoryGateway.sendEmailToValidateEmail(user);

        if(!confirmationCode.equals(code)){
            throw new IncorrectCodeException();
        }
    }

    private void validatePassword(String rawPassword, String storedPassword) {
        if (!passwordEncoder.matches(rawPassword, storedPassword)) {
            throw new IncorrectPasswordException();
        }
    }

    public UserDetails findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(EmailIsNotYetRegisteredException::new);
    }
}
