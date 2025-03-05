package com.crewmeister.cmcodingchallenge.load;

import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.util.BundesbankClient;
import com.crewmeister.cmcodingchallenge.util.JsonParser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExchangeRateAndCurrencyDataLoaderOnStartup implements ApplicationListener<ApplicationReadyEvent> {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ExchangeRateAndCurrencyDataLoaderOnStartup.class);
    @Autowired
    JsonParser jsonParser;

    @Autowired
    BundesbankClient bundesbankClientUtil;

    @Autowired
    CurrencyRepository currencyRepository;

    @Value("${bundesbank.exchangeRates.url}")
    private String exchangeRateUrl;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        loadCurrency();
        loadExchangeRates();
    }

    private void loadCurrency() {
        logger.info("Please wait while Currency data is loaded from bundesbank");
        String currencyData = bundesbankClientUtil.getCurrencyDataAsString();
        List<Currency> currencyList = new ArrayList<>();

        try {
            currencyList = jsonParser.parseCurrencyJson(currencyData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        currencyRepository.saveAll(currencyList);
        logger.info("All Currency data are loaded from bundesbank Successfully!!!");
    }

    private void loadExchangeRates()
    {
        logger.info("Please wait while ExchangeRates are loaded from bundesbank");
        String exchangeRateData = bundesbankClientUtil.getExchangeRateDataAsString(exchangeRateUrl);

        try {
            jsonParser.parseExchangeRate(exchangeRateData);
            logger.info("Exchange Rates are loaded from bundesbank Successfully!!!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
