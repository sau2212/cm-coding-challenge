package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.config.CmCodingChallengeTestConfigurer;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {CmCodingChallengeTestConfigurer.class})
@AutoConfigureMockMvc
class ExchangeRateControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRateController exchangeRateController;

    private ExchangeRate exchangeRate;
    private List<ExchangeRate> exchangeRateList;

    @BeforeEach
    void setUp() {
        // Setting up some dummy data for testing
        exchangeRate = new ExchangeRate("USD", LocalDate.of(2025, 3, 2), new BigDecimal("1.1"));
        exchangeRateList = Arrays.asList(
                new ExchangeRate("USD", LocalDate.of(2025, 3, 2), new BigDecimal("1.1")),
                new ExchangeRate("SEK", LocalDate.of(2025, 3, 2), new BigDecimal("10.5"))
        );
    }

    @Test
    void testGetExchangeRatesForCurrency() throws Exception {
        when(exchangeRateService.getExchangeRatesForAllCurrency()).thenReturn(exchangeRateList);

       mockMvc.perform(get("/api/exchange-rates")
                        .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currencyCode").value("USD"))
                .andExpect(jsonPath("$[1].currencyCode").value("SEK"))
                .andExpect(jsonPath("$[0].rate").value(1.1))
                .andExpect(jsonPath("$[1].rate").value(10.5));
        verify(exchangeRateService, times(1)).getExchangeRatesForAllCurrency();
    }

    @Test
    void testGetExchangeRatesForCurrency_serviceThrowsException() throws Exception {

        when(exchangeRateService.getExchangeRatesForAllCurrency()).thenThrow(new RuntimeException("Some Service exception"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exchange-rates"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetExchangeRateForCurrency() throws Exception {
        when(exchangeRateService.getExchangeRateForCurrencyOnDate("USD", LocalDate.of(2025, 3, 2)))
                .thenReturn(new ExchangeRate("USD", LocalDate.of(2025, 3, 2), new BigDecimal("1.1")));

        mockMvc.perform(get("/api/exchange-rate/{date}", "2025-03-02")
                        .param("currencyCode", "USD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.rate").value(1.1));

        when(exchangeRateService.getExchangeRateByDate(LocalDate.of(2025, 3, 2))).thenReturn(exchangeRateList);

        mockMvc.perform(get("/api/exchange-rate/{date}", "2025-03-02")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].currencyCode").value("USD"))
                .andExpect(jsonPath("[1].currencyCode").value("SEK"));

        verify(exchangeRateService, times(1)).getExchangeRateForCurrencyOnDate(anyString(), any());
        verify(exchangeRateService, times(1)).getExchangeRateByDate(any());
    }

    @Test
    void testGetExchangeRateForCurrency_serviceThrowsException() throws Exception {

        when(exchangeRateService.getExchangeRateByDate(any())).thenThrow(new RuntimeException("Some exception"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exchange-rate/{date}", "2025-03-02"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testConvertToEUR() throws Exception {
        BigDecimal amount = new BigDecimal("200");
        LocalDate date = LocalDate.of(2025, 3, 2);
        BigDecimal convertedAmount = new BigDecimal("220");

        when(exchangeRateService.convertToEUR("USD", amount, date)).thenReturn(convertedAmount);

        mockMvc.perform(get("/api/convert")
                        .param("currencyCode", "USD")
                        .param("amount", "200")
                        .param("date", "2025-03-02")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("220"));

        verify(exchangeRateService, times(1)).convertToEUR(anyString(), any(), any());
    }

    @Test
    void testConvertToEUR_serviceThrowsException() throws Exception {

        when(exchangeRateService.convertToEUR(anyString(), any(), any())).thenThrow(new RuntimeException("Exception Occured!!"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/convert")
                .param("currencyCode", "USD")
                .param("amount", "200")
                .param("date", "2025-03-02"))
                .andExpect(status().isInternalServerError());
    }

}