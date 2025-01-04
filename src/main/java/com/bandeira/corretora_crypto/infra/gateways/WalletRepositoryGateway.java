package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.application.gateways.WalletGateway;
import com.bandeira.corretora_crypto.domain.User;
import com.bandeira.corretora_crypto.domain.Wallet;
import com.bandeira.corretora_crypto.domain.enums.TransactionType;
import com.bandeira.corretora_crypto.infra.dtos.AssetDTO;
import com.bandeira.corretora_crypto.infra.dtos.CalculateProfitOrLoss;
import com.bandeira.corretora_crypto.infra.dtos.FindByWalletResponseDTO;
import com.bandeira.corretora_crypto.infra.exceptions.WalletNotFound;
import com.bandeira.corretora_crypto.infra.persistence.TransactionEntity;

import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import com.bandeira.corretora_crypto.infra.persistence.repository.WalletRepository;
import com.bandeira.corretora_crypto.infra.util.UserMapper;
import com.bandeira.corretora_crypto.infra.util.WalletMapper;

import java.math.BigDecimal;
import java.util.List;


public class WalletRepositoryGateway implements WalletGateway{

    private final WalletMapper walletMapper;

    private final WalletRepository walletRepository;

    private final TransactionGatewayRepository transactionGatewayRepository;

    private final CryptoRepositoryGateway cryptoRepositoryGateway;

    private UserMapper userMapper;

    public WalletRepositoryGateway(WalletMapper walletMapper
            , WalletRepository walletRepository
            , TransactionGatewayRepository transactionGatewayRepository
            , CryptoRepositoryGateway cryptoRepositoryGateway, UserMapper userMapper) {
        this.walletMapper = walletMapper;
        this.walletRepository = walletRepository;
        this.transactionGatewayRepository = transactionGatewayRepository;
        this.cryptoRepositoryGateway = cryptoRepositoryGateway;
        this.userMapper = userMapper;
    }

    @Override
    public Wallet findByWalletById(Long id) {
        var walletEntity = walletRepository.findById(id).orElseThrow(WalletNotFound::new);

        return walletMapper.toWallet(walletEntity);
    }

    public FindByWalletResponseDTO findWalletById(Long walletId) {
            var wallet = findByWalletById(walletId);

        List<TransactionEntity> transactions = transactionGatewayRepository
                .findByTransactionByUserId(wallet.getUser().getId());

        var  totalInvested = calculateTotalInvested(wallet.getUser().getId());

        var assets = calculateAssets(wallet);

        var profitOrLoss = calculateProfitOrLoss(new CalculateProfitOrLoss(
                wallet.getCurrentValueInvested(), totalInvested));

        return new FindByWalletResponseDTO(
                totalInvested,
                wallet.getCurrentValueInvested(),
                profitOrLoss,
                assets
        );
    }

    private BigDecimal calculateTotalInvested(Long userId){
        var transactions = transactionGatewayRepository.findByTransactionByUserId(userId);

        return transactions.stream()
                .map(TransactionEntity::getPriceAtTransaction)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<AssetDTO> calculateAssets(Wallet wallet){

        List<AssetDTO> assets = wallet.getAssets().entrySet().stream()
                .map(entry -> {
                    String cryptoName = entry.getKey();
                    BigDecimal quantity = entry.getValue();

                    BigDecimal currentPrice = cryptoRepositoryGateway
                            .findCryptoPriceByName(cryptoName);

                    BigDecimal currentValue = currentPrice.multiply(quantity);

                    BigDecimal averagePrice = calculateAveragePrice(
                            transactionGatewayRepository.findByTransactionByUserId(wallet.getUser().getId())
                            , cryptoName);

                    return new AssetDTO(cryptoName, quantity, averagePrice, currentPrice, currentValue);
                })
                .toList();

        return assets;
    }

    public void CreateWallet(UserEntity user){
        var wallet = walletMapper.newWalletEntity(user);

        walletRepository.save(wallet);
    }

    private BigDecimal calculateAveragePrice(List<TransactionEntity> transactions, String cryptoName) {
        List<BigDecimal> purchasePrices = transactions.stream()
                .filter(t -> t.getCrypto().getName().equals(cryptoName)
                        && t.getTransactionType() == TransactionType.BUY)
                .map(TransactionEntity::getPriceAtTransaction)
                .toList();

        if (purchasePrices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = purchasePrices.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(purchasePrices.size()), BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculateProfitOrLoss(CalculateProfitOrLoss request){
        return request.currentValue().subtract(request.totalValue());
    }


}
