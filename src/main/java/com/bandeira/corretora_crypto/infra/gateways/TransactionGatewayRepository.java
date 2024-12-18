package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.application.gateways.TransactionGateway;
import com.bandeira.corretora_crypto.infra.dtos.*;
import com.bandeira.corretora_crypto.infra.exceptions.InsufficientBalanceException;
import com.bandeira.corretora_crypto.infra.exceptions.InsufficientCryptoException;
import com.bandeira.corretora_crypto.infra.persistence.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionGatewayRepository implements TransactionGateway{

    private final UserRepositoryGateway userRepositoryGateway;

    private final CryptoRepositoryGateway cryptoRepositoryGateway;

    private final WalletRepository walletRepository;

    public TransactionGatewayRepository(UserRepositoryGateway userRepositoryGateway
            , CryptoRepositoryGateway cryptoRepositoryGateway, WalletRepository walletRepository) {
        this.userRepositoryGateway = userRepositoryGateway;
        this.cryptoRepositoryGateway = cryptoRepositoryGateway;
        this.walletRepository = walletRepository;
    }


    @Override
    public void buyCrypto(BuyCryptoDTO request) {
        var user = userRepositoryGateway.findById(request.userId());
        var crypto = cryptoRepositoryGateway.findByName(request.cryptoName());

        var total = validateBuyTransaction(new ValidateBuyDTO(user, crypto, request.quantity()));

        updateBuyWallet(new UpdateWalletDTO(user, crypto, total, request.quantity()));
    }


    @Override
    public void sellCrypto(SellCryptoDTO request) {
        var user = userRepositoryGateway.findById(request.userId());
        var crypto = cryptoRepositoryGateway.findByName(request.cryptoName());

        var total = validateSellTransaction(new ValidateSellDTO(user, crypto, request.quantity()));

        updateSellWallet(new UpdateWalletDTO(user, crypto, total, request.quantity()));
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


}
