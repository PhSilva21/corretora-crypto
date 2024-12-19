package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.application.gateways.TransactionGateway;
import com.bandeira.corretora_crypto.domain.Crypto;
import com.bandeira.corretora_crypto.domain.enums.TransactionType;
import com.bandeira.corretora_crypto.infra.dtos.*;
import com.bandeira.corretora_crypto.infra.exceptions.CryptoNotFoundException;
import com.bandeira.corretora_crypto.infra.exceptions.InsufficientBalanceException;
import com.bandeira.corretora_crypto.infra.exceptions.InsufficientCryptoException;
import com.bandeira.corretora_crypto.infra.exceptions.UserNotFoundException;
import com.bandeira.corretora_crypto.infra.persistence.TransactionEntity;
import com.bandeira.corretora_crypto.infra.persistence.repository.CryptoRepository;
import com.bandeira.corretora_crypto.infra.persistence.repository.TransactionRepository;
import com.bandeira.corretora_crypto.infra.persistence.repository.UserRepository;
import com.bandeira.corretora_crypto.infra.persistence.repository.WalletRepository;
import com.bandeira.corretora_crypto.infra.util.TransactionMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionGatewayRepository implements TransactionGateway{

    private final UserRepositoryGateway userRepositoryGateway;

    private final CryptoRepositoryGateway cryptoRepositoryGateway;

    private final WalletRepository walletRepository;

    private final TransactionMapper transactionMapper;

    private final UserRepository userRepository;

    private final CryptoRepository cryptoRepository;

    private final TransactionRepository transactionRepository;

    public TransactionGatewayRepository(UserRepositoryGateway userRepositoryGateway
            , CryptoRepositoryGateway cryptoRepositoryGateway, WalletRepository walletRepository
            , TransactionMapper transactionMapper, UserRepository userRepository
            , CryptoRepository cryptoRepository, TransactionRepository transactionRepository) {
        this.userRepositoryGateway = userRepositoryGateway;
        this.cryptoRepositoryGateway = cryptoRepositoryGateway;
        this.walletRepository = walletRepository;
        this.transactionMapper = transactionMapper;
        this.userRepository = userRepository;
        this.cryptoRepository = cryptoRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void buyCrypto(BuyCryptoDTO request) {
        var user = userRepositoryGateway.findById(request.userId());
        var crypto = cryptoRepositoryGateway.findCryptoByNameToBuy(request.cryptoName());

        var total = validateBuyTransaction(new ValidateBuyDTO(user, crypto, request.quantity()));

        saveTransaction(new SaveTransactionDTO(user, crypto, request.quantity(), total));

        updateBuyWallet(new UpdateWalletDTO(user, crypto, total, request.quantity()
                , TransactionType.BUY));
    }


    @Override
    public void sellCrypto(SellCryptoDTO request) {
        var user = userRepositoryGateway.findById(request.userId());
        var crypto = cryptoRepositoryGateway.findByName(request.cryptoName());

        var total = validateSellTransaction(new ValidateSellDTO(user, crypto, request.quantity()));

        saveTransaction(new SaveTransactionDTO(user, crypto, request.quantity(), total));

        updateSellWallet(new UpdateWalletDTO(user, crypto, total, request.quantity()
                , TransactionType.SELL));
    }


    @Override
    public BigDecimal validateBuyTransaction(ValidateBuyDTO request) {
        var total = request.crypto().getPrice().multiply(request.quantity());

        if(request.user().getWallet().getBalance().compareTo(total) < 0){
            throw new InsufficientBalanceException();
        }
        return total;
    }


    @Override
    public BigDecimal validateSellTransaction(ValidateSellDTO request) {
        var quantity = request.user().getWallet().getAssets().get(request.crypto().getName());

        if(quantity.compareTo(request.quantitySell()) < 0){
            throw new InsufficientCryptoException();
        }
        return request.quantitySell().multiply(request.crypto().getPrice());
    }


    @Override
    public void updateBuyWallet(UpdateWalletDTO request){
        request.user().getWallet().setBalance(request.user().getWallet().getBalance()
                .subtract(request.totalPrice()));

        request.user().getWallet().getAssets().merge(request.crypto().getName()
                , request.quantityCrypto(), BigDecimal::add);

        if(request.transactionType().equals(TransactionType.BUY)){
            request.user().getWallet().addInvestment(request.totalPrice());
        }

        request.user().getWallet().subtractInvestment(request.totalPrice());

        walletRepository.save(request.user().getWallet());
    }


    @Override
    public void updateSellWallet(UpdateWalletDTO request){
        request.user().getWallet().setBalance(request.user().getWallet().getBalance()
                .add(request.totalPrice()));

        var updatedQuantity = request.user().getWallet().getAssets().get(request.crypto().getName()
        );

        if (updatedQuantity.compareTo(BigDecimal.ZERO) == 0) {
            request.user().getWallet().getAssets().remove(request.crypto().getName());
        }

        request.user().getWallet().getAssets().put(request.crypto().getName(), updatedQuantity);

        walletRepository.save(request.user().getWallet());
    }

    public void saveTransaction(SaveTransactionDTO request) {
        var user = userRepository.findById(request.user().getId())
                .orElseThrow(UserNotFoundException::new);

        var crypto = cryptoRepository.findByName(request.crypto().getName())
                .orElseThrow(CryptoNotFoundException::new);

        var transaction = transactionMapper.toTransactionEntity(new CreateTransactionDTO(user, crypto
                , request.quantity(), request.total(), LocalDateTime.now()
                , TransactionType.BUY));

        crypto.incrementPopularity();

        transactionRepository.save(transaction);
    }

    public List<TransactionEntity> findByTransactionByUserId(Long id){
        return transactionRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(id)).toList();
    }
}
