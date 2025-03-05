package com.crewmeister.cmcodingchallenge.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import com.crewmeister.cmcodingchallenge.config.CmCodingChallengeTestConfigurer;
import com.crewmeister.cmcodingchallenge.exception.ExchangeRateNotFoundException;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CmCodingChallengeTestConfigurer.class})
class ExchangeRateServiceImplTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    private ExchangeRate exchangeRate;

    @BeforeEach
    void setUp() {
        exchangeRate = new ExchangeRate();
        exchangeRate.setCurrency("USD");
        exchangeRate.setRate(new BigDecimal("1.2"));
        exchangeRate.setDate(LocalDate.of(2025, 3, 3));
    }

    @Test
    void testGetExchangeRatesForAllCurrency() throws ExchangeRateNotFoundException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRate);

        when(exchangeRateRepository.findAll()).thenReturn(exchangeRates);

        List<ExchangeRate> result = exchangeRateService.getExchangeRatesForAllCurrency();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).getCurrency());
    }

    @Test
    void testGetExchangeRateForCurrencyOnDate() throws ExchangeRateNotFoundException {
        when(exchangeRateRepository.findByCurrencyAndDate("USD", LocalDate.of(2025, 3, 3)))
                .thenReturn(exchangeRate);
        ExchangeRate result = exchangeRateService.getExchangeRateForCurrencyOnDate("USD", LocalDate.of(2025, 3, 3));

        assertNotNull(result);
        assertEquals("USD", result.getCurrency());
        assertEquals(new BigDecimal("1.2"), result.getRate());
    }

    @Test
    void testGetExchangeRateForCurrencyOnDate_notFound() {
        when(exchangeRateRepository.findByCurrencyAndDate("USD", LocalDate.of(2025, 3, 3)))
                .thenReturn(null);

        ExchangeRateNotFoundException exception = assertThrows(ExchangeRateNotFoundException.class, () -> {
            exchangeRateService.getExchangeRateForCurrencyOnDate("USD", LocalDate.of(2025, 3, 3));
        });

        assertEquals("Exchange rate not found", exception.getMessage());
    }

    @Test
    void testGetExchangeRateByDate() throws ExchangeRateNotFoundException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRate);

        when(exchangeRateRepository.findByDate(LocalDate.of(2025, 3, 3)))
                .thenReturn(exchangeRates);

        List<ExchangeRate> result = exchangeRateService.getExchangeRateByDate(LocalDate.of(2025, 3, 3));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).getCurrency());
    }

    @Test
    void testConvertToEUR() throws Exception{
        when(exchangeRateRepository.findByCurrencyAndDate("USD", LocalDate.of(2025, 3, 3)))
                .thenReturn(exchangeRate);

        BigDecimal amount = new BigDecimal("100");

        BigDecimal result = exchangeRateService.convertToEUR("USD", amount, LocalDate.of(2025, 3, 3));

        assertNotNull(result);
        assertEquals(new BigDecimal("83.33333333333333").setScale(2, RoundingMode.HALF_UP), result);
    }

    @Test
    void testConvertToEUR_exchangeRateNotFound() {
        when(exchangeRateRepository.findByCurrencyAndDate("USD", LocalDate.of(2025, 3, 3)))
                .thenReturn(null);

        BigDecimal amount = new BigDecimal("100");
        ExchangeRateNotFoundException exception = assertThrows(ExchangeRateNotFoundException.class, () -> {
            exchangeRateService.convertToEUR("USD", amount, LocalDate.of(2025, 3, 3));
        });

        assertEquals("Exchange rate not found", exception.getMessage());
    }

}