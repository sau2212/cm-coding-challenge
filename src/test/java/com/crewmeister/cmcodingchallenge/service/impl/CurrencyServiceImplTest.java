package com.crewmeister.cmcodingchallenge.service.impl;

import com.crewmeister.cmcodingchallenge.config.CmCodingChallengeTestConfigurer;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CmCodingChallengeTestConfigurer.class})
class CurrencyServiceImplTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    private Currency currency;

    @BeforeEach
    void setUp() {
        currency = new Currency();
        currency.setName("USD");
        currency.setCode("USD");
    }

    @Test
    void testGetAllCurrencies() {
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(currency);
        when(currencyRepository.findAll()).thenReturn(currencyList);

        List<Currency> result = currencyService.getAllCurrencies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).getName());
    }

    @Test
    void testGetAllCurrencies_emptyList() {

        when(currencyRepository.findAll()).thenReturn(Collections.emptyList());
        List<Currency> result = currencyService.getAllCurrencies();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}