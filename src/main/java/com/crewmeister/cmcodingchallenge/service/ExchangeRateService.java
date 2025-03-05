package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.exception.ExchangeRateNotFoundException;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateService {

    List<ExchangeRate> getExchangeRatesForAllCurrency() throws ExchangeRateNotFoundException;
    ExchangeRate getExchangeRateForCurrencyOnDate(String currencyCode, LocalDate date) throws ExchangeRateNotFoundException;
    List<ExchangeRate> getExchangeRateByDate(LocalDate date) throws ExchangeRateNotFoundException;

    BigDecimal convertToEUR(String currencyCode, BigDecimal amount, LocalDate date) throws ExchangeRateNotFoundException;
}
