package com.bandeira.corretora_crypto.application.gateways;

import com.bandeira.corretora_crypto.infra.dtos.*;

import java.math.BigDecimal;

public interface TransactionGateway {

    void buyCrypto(BuyCryptoDTO request);

    void sellCrypto(SellCryptoDTO request);

    BigDecimal validateBuyTransaction(ValidateBuyDTO request);

    BigDecimal validateSellTransaction(ValidateSellDTO request);

    void updateBuyWallet(UpdateWalletDTO request);

    void updateSellWallet(UpdateWalletDTO request);
}
