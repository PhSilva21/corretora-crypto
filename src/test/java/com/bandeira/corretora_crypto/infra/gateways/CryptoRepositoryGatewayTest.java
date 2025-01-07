package com.bandeira.corretora_crypto.infra.gateways;

import com.bandeira.corretora_crypto.domain.Crypto;
import com.bandeira.corretora_crypto.infra.exceptions.CryptoNotFoundException;
import com.bandeira.corretora_crypto.infra.persistence.CryptoEntity;
import com.bandeira.corretora_crypto.infra.persistence.repository.CryptoRepository;
import com.bandeira.corretora_crypto.infra.util.CryptoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CryptoRepositoryGatewayTest {

    @Mock
    private CryptoRepository cryptoRepository;

    @Mock
    private CryptoMapper cryptoMapper;

    @InjectMocks
    private CryptoRepositoryGateway cryptoRepositoryGateway;

    private CryptoEntity testCryptoEntity;
    private Crypto testCrypto;
    private List<CryptoEntity> testCryptoEntities;
    private List<Crypto> testCryptos;

    @BeforeEach
    void setUp() {
        testCryptoEntity = new CryptoEntity();
        testCryptoEntity.setId(1L);
        testCryptoEntity.setName("Bitcoin");
        testCryptoEntity.setSymbol("BTC");
        testCryptoEntity.setPrice(new BigDecimal("50000.00"));
        testCryptoEntity.setPopularity(100);
        testCryptoEntity.setLaunchDate(LocalDate.now().minusYears(1));

        testCrypto = new Crypto(
                1L,
                "BTC",
                "Bitcoin",
                new BigDecimal("50000.00"),
                100,
                LocalDate.now().minusYears(1),
                List.of()
        );

        testCryptoEntities = List.of(
                testCryptoEntity,
                createCryptoEntity("Ethereum", "ETH", new BigDecimal("3000.00")
                        , 80),
                createCryptoEntity("Dogecoin", "DOGE", new BigDecimal("0.20")
                        , 60)
        );

        testCryptos = List.of(
                testCrypto,
                createCrypto("Ethereum", "ETH", new BigDecimal("3000.00"), 80),
                createCrypto("Dogecoin", "DOGE", new BigDecimal("0.20"), 60)
        );
    }

    private CryptoEntity createCryptoEntity(String name, String symbol, BigDecimal price, int popularity) {
        CryptoEntity crypto = new CryptoEntity();
        crypto.setName(name);
        crypto.setSymbol(symbol);
        crypto.setPrice(price);
        crypto.setPopularity(popularity);
        crypto.setLaunchDate(LocalDate.now().minusMonths(popularity));
        return crypto;
    }

    private Crypto createCrypto(String name, String symbol, BigDecimal price, int popularity) {
        return new Crypto(
                null,
                symbol,
                name,
                price,
                popularity,
                LocalDate.now().minusMonths(popularity),
                List.of()
        );
    }

    @Test
    void findByName_ShouldReturnCrypto_WhenExists() {
        when(cryptoRepository.findByName("Bitcoin")).thenReturn(Optional.of(testCryptoEntity));
        when(cryptoMapper.toCrypto(testCryptoEntity)).thenReturn(testCrypto);

        Crypto result = cryptoRepositoryGateway.findByName("Bitcoin");

        assertNotNull(result);
        assertEquals("Bitcoin", result.getName());
        assertEquals("BTC", result.getSymbol());
        verify(cryptoRepository).findByName("Bitcoin");
        verify(cryptoMapper).toCrypto(testCryptoEntity);
    }

    @Test
    void findByName_ShouldThrowException_WhenNotExists() {
        when(cryptoRepository.findByName("NonexistentCoin"))
                .thenReturn(Optional.empty());

        assertThrows(CryptoNotFoundException.class,
                () -> cryptoRepositoryGateway.findByName("NonexistentCoin"));
        verify(cryptoRepository).findByName("NonexistentCoin");
    }

    @Test
    void findByPopularity_ShouldReturnTopTenByPopularity() {
        when(cryptoRepository.findAll()).thenReturn(testCryptoEntities);

        List<Crypto> result = cryptoRepositoryGateway.findByPopularity();

        assertNotNull(result);
        assertTrue(result.size() <= 10);

        for (int i = 1; i < result.size(); i++) {
            assertTrue(result.get(i-1).getPopularity() >= result.get(i).getPopularity());
        }
    }

    @Test
    void findAll_ShouldReturnAllCryptos() {
        when(cryptoRepository.findAll()).thenReturn(testCryptoEntities);

        List<Crypto> result = cryptoRepositoryGateway.findAll();

        assertNotNull(result);
        assertEquals(testCryptoEntities.size(), result.size());
        verify(cryptoRepository).findAll();
    }

    @Test
    void findByRecentLaunch_ShouldReturnTopTenByLaunchDate() {
        when(cryptoRepository.findAll()).thenReturn(testCryptoEntities);

        List<Crypto> result = cryptoRepositoryGateway.findByRecentLaunch();

        assertNotNull(result);
        assertTrue(result.size() <= 10);

        for (int i = 1; i < result.size(); i++) {
            assertTrue(result.get(i-1).getLaunchDate()
                    .isAfter(result.get(i).getLaunchDate())
                    || result.get(i-1).getLaunchDate()
                    .isEqual(result.get(i).getLaunchDate()));
        }
    }

    @Test
    void findCryptoPriceByName_ShouldReturnPrice() {
        when(cryptoRepository.findByName("Bitcoin")).thenReturn(Optional.of(testCryptoEntity));
        when(cryptoMapper.toCrypto(testCryptoEntity)).thenReturn(testCrypto);

        BigDecimal result = cryptoRepositoryGateway.findCryptoPriceByName("Bitcoin");

        assertEquals(new BigDecimal("50000.00"), result);
    }

    @Test
    void findCryptoByNameToBuy_ShouldIncrementPopularityAndSave() {
        when(cryptoRepository.findByName("Bitcoin")).thenReturn(Optional.of(testCryptoEntity));
        when(cryptoMapper.toCrypto(any())).thenReturn(testCrypto);

        int initialPopularity = testCryptoEntity.getPopularity();

        Crypto result = cryptoRepositoryGateway.findCryptoByNameToBuy("Bitcoin");

        assertNotNull(result);
        assertEquals(initialPopularity + 1, testCryptoEntity.getPopularity());
        verify(cryptoRepository).save(testCryptoEntity);
        verify(cryptoMapper).toCrypto(testCryptoEntity);
    }

    @Test
    void findCryptoByNameToBuy_ShouldThrowException_WhenNotExists() {
        when(cryptoRepository.findByName("NonexistentCoin"))
                .thenReturn(Optional.empty());

        assertThrows(CryptoNotFoundException.class,
                () -> cryptoRepositoryGateway.findCryptoByNameToBuy("NonexistentCoin"));
    }
}