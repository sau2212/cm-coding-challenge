package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.config.CmCodingChallengeTestConfigurer;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {CmCodingChallengeTestConfigurer.class})
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    private List<Currency> mockCurrencies;

    @BeforeEach
    void setUp() {
        mockCurrencies = Arrays.asList(
                new Currency("USD", "United States Dollar"),
                new Currency("EUR", "Euro"),
                new Currency("JPY", "Japanese Yen")
        );
    }

    @Test
    void testGetCurrencies() throws Exception {

        when(currencyService.getAllCurrencies()).thenReturn(mockCurrencies);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code").value("USD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("United States Dollar"));
    }

    @Test
    void testGetCurrencies_noData() throws Exception {

        when(currencyService.getAllCurrencies()).thenReturn(Arrays.asList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    void testGetCurrencies_serviceThrowsException() throws Exception {

        when(currencyService.getAllCurrencies()).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/currencies"))
                .andExpect(status().isInternalServerError());
    }

}