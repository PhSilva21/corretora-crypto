package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.domain.Wallet;

import com.bandeira.corretora_crypto.domain.enums.TransactionType;
import com.bandeira.corretora_crypto.infra.dtos.AssetDTO;
import com.bandeira.corretora_crypto.infra.dtos.CalculateProfitOrLoss;
import com.bandeira.corretora_crypto.infra.dtos.FindByWalletResponseDTO;
import com.bandeira.corretora_crypto.infra.exceptions.WalletNotFound;
import com.bandeira.corretora_crypto.infra.persistence.CryptoEntity;
import com.bandeira.corretora_crypto.infra.persistence.TransactionEntity;
import com.bandeira.corretora_crypto.infra.persistence.UserEntity;
import com.bandeira.corretora_crypto.infra.persistence.WalletEntity;
import com.bandeira.corretora_crypto.infra.persistence.repository.WalletRepository;
import com.bandeira.corretora_crypto.infra.util.WalletMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WalletRepositoryGatewayTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletMapper walletMapper;

    @Mock
    private TransactionGatewayRepository transactionGatewayRepository;

    @Mock
    private CryptoRepositoryGateway cryptoRepositoryGateway;

    @InjectMocks
    private WalletRepositoryGateway walletService;

    private UserEntity testUser;
    private WalletEntity testWalletEntity;
    private Wallet testWallet;
    private List<TransactionEntity> testTransactions;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);

        testWalletEntity = new WalletEntity();
        testWalletEntity.setId(1L);
        testWalletEntity.setUser(testUser);

        testWallet = new Wallet();
        testWallet.setUser(testUser);
        testWallet.setCurrentValueInvested(new BigDecimal("1000.00"));
        HashMap<String, BigDecimal> assets = new HashMap<>();
        assets.put("BTC", new BigDecimal("0.5"));
        testWallet.setAssets(assets);

        testTransactions = List.of(
                createTransaction("BTC", new BigDecimal("100.00"), TransactionType.BUY),
                createTransaction("BTC", new BigDecimal("150.00"), TransactionType.BUY)
        );
    }

    private TransactionEntity createTransaction(String crypto, BigDecimal price, TransactionType type) {
        TransactionEntity transaction = new TransactionEntity();
        CryptoEntity cryptoEntity = new CryptoEntity();
        cryptoEntity.setName(crypto);
        transaction.setCrypto(cryptoEntity);
        transaction.setPriceAtTransaction(price);
        transaction.setTransactionType(type);
        return transaction;
    }

    @Test
    void findByWalletById_ShouldReturnWallet_WhenWalletExists() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(testWalletEntity));
        when(walletMapper.toWallet(testWalletEntity)).thenReturn(testWallet);

        Wallet result = walletService.findByWalletById(1L);

        assertNotNull(result);
        assertEquals(testWallet, result);
        verify(walletRepository).findById(1L);
        verify(walletMapper).toWallet(testWalletEntity);
    }

    @Test
    void findByWalletById_ShouldThrowWalletNotFound_WhenWalletDoesNotExist() {
        when(walletRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFound.class, () -> walletService.findByWalletById(1L));
        verify(walletRepository).findById(1L);
    }

    @Test
    void findWalletById_ShouldReturnCorrectDTO() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(testWalletEntity));
        when(walletMapper.toWallet(testWalletEntity)).thenReturn(testWallet);
        when(transactionGatewayRepository.findByTransactionByUserId(1L)).thenReturn(testTransactions);
        when(cryptoRepositoryGateway.findCryptoPriceByName("BTC")).thenReturn(new BigDecimal("40000.00"));

        FindByWalletResponseDTO result = walletService.findWalletById(1L);

        assertNotNull(result);
        assertEquals(new BigDecimal("250.00"), result.totalInvested());
        assertEquals(new BigDecimal("1000.00"), result.currentTotalValue());
        assertEquals(new BigDecimal("750.00"), result.profitOrLoss());
        assertFalse(result.assets().isEmpty());
    }

    @Test
    void calculateTotalInvested_ShouldReturnCorrectTotal() {
        when(transactionGatewayRepository.findByTransactionByUserId(1L)).thenReturn(testTransactions);

        BigDecimal result = walletService.calculateTotalInvested(1L);

        assertEquals(new BigDecimal("250.00"), result);
        verify(transactionGatewayRepository).findByTransactionByUserId(1L);
    }

    @Test
    void calculateAssets_ShouldReturnCorrectAssetsList() {
        when(cryptoRepositoryGateway.findCryptoPriceByName("BTC")).thenReturn(new BigDecimal("40000.00"));
        when(transactionGatewayRepository.findByTransactionByUserId(1L)).thenReturn(testTransactions);

        List<AssetDTO> result = walletService.calculateAssets(testWallet);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        AssetDTO btcAsset = result.get(0);
        assertEquals("BTC", btcAsset.cryptoName());
        assertEquals(new BigDecimal("0.5"), btcAsset.quantity());
        assertEquals(new BigDecimal("125.00"), btcAsset.averagePrice());
        assertEquals(new BigDecimal("40000.00"), btcAsset.currentPrice());
        assertEquals(new BigDecimal("20000.000"), btcAsset.currentValue());
    }

    @Test
    void createWallet_ShouldSaveWallet() {
        WalletEntity newWalletEntity = new WalletEntity();
        when(walletMapper.newWalletEntity(testUser)).thenReturn(newWalletEntity);

        walletService.CreateWallet(testUser);

        verify(walletMapper).newWalletEntity(testUser);
        verify(walletRepository).save(newWalletEntity);
    }

    @Test
    void calculateAveragePrice_ShouldReturnCorrectAverage() {
        List<TransactionEntity> transactions = List.of(
                createTransaction("BTC", new BigDecimal("100.00"), TransactionType.BUY),
                createTransaction("BTC", new BigDecimal("200.00"), TransactionType.BUY),
                createTransaction("BTC", new BigDecimal("150.00"), TransactionType.SELL)
        );

        BigDecimal result = walletService.calculateAveragePrice(transactions, "BTC");

        assertEquals(new BigDecimal("150.00"), result);
    }

    @Test
    void calculateAveragePrice_ShouldReturnZero_WhenNoTransactions() {
        List<TransactionEntity> emptyTransactions = List.of();

        BigDecimal result = walletService.calculateAveragePrice(emptyTransactions, "BTC");

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateProfitOrLoss_ShouldReturnCorrectValue() {
        CalculateProfitOrLoss request = new CalculateProfitOrLoss(
                new BigDecimal("1000.00"),
                new BigDecimal("800.00")
        );

        BigDecimal result = walletService.calculateProfitOrLoss(request);

        assertEquals(new BigDecimal("200.00"), result);
    }
}