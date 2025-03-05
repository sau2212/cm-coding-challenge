package com.crewmeister.cmcodingchallenge.service.impl;


import com.crewmeister.cmcodingchallenge.exception.ExchangeRateNotFoundException;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.ListUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public List<ExchangeRate> getExchangeRatesForAllCurrency() throws ExchangeRateNotFoundException {
        List<ExchangeRate> exchangeRateList= exchangeRateRepository.findAll();
        if(ListUtils.isEmpty(exchangeRateList))
            throw new ExchangeRateNotFoundException("Exchange rate not found");
        return exchangeRateRepository.findAll();
    }

    public ExchangeRate getExchangeRateForCurrencyOnDate(String currencyCode, LocalDate date) throws ExchangeRateNotFoundException {
        ExchangeRate exchangeRate= exchangeRateRepository.findByCurrencyAndDate(currencyCode, date);
        if(ObjectUtils.isEmpty(exchangeRate))
            throw new ExchangeRateNotFoundException("Exchange rate not found");
        return exchangeRate;
    }

    public List<ExchangeRate> getExchangeRateByDate(LocalDate date) throws ExchangeRateNotFoundException {
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findByDate(date);
        if(ListUtils.isEmpty(exchangeRateList))
            throw new ExchangeRateNotFoundException("Exchange rate not found");
        return exchangeRateList;
    }

    public BigDecimal convertToEUR(String currencyCode, BigDecimal amount, LocalDate date) throws ExchangeRateNotFoundException {
        ExchangeRate rate = getExchangeRateForCurrencyOnDate(currencyCode, date);
        if (rate != null) {
            return amount.divide(rate.getRate(), 2, RoundingMode.HALF_UP); // Convert amount to EUR
        }
        throw new ExchangeRateNotFoundException("Unable to Convert to EURO as Exchange rate not found");
    }

}
