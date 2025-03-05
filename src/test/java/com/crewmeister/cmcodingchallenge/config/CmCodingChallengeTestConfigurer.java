package com.crewmeister.cmcodingchallenge.config;

import com.crewmeister.cmcodingchallenge.load.ExchangeRateAndCurrencyDataLoaderOnStartup;
import com.crewmeister.cmcodingchallenge.service.impl.ExchangeRateServiceImpl;
import com.crewmeister.cmcodingchallenge.util.BundesbankClient;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

@ComponentScan(value = {"com.crewmeister.cmcodingchallenge"})
public class CmCodingChallengeTestConfigurer {
    @MockBean
    private ExchangeRateAndCurrencyDataLoaderOnStartup exchangeRateAndCurrencyDataLoaderOnStartup;

    @MockBean
    BundesbankClient bundesbankConnectionUtil;

    @Mock
    RestTemplate restTemplate;

}
