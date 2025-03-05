package com.crewmeister.cmcodingchallenge.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class BundesbankClient {

    @Value("${bundesbank.currency.url}")
    private String currencyUrl;

    @Autowired
    RestTemplate restTemplate;

    public String getCurrencyDataAsString()
    {
        ResponseEntity<String> response = restTemplate.exchange(currencyUrl, HttpMethod.GET,null, String.class);
        return response.getBody();
    }

    public String getExchangeRateDataAsString(String exchangeRateUrl)
    {
        ResponseEntity<String> response = restTemplate.exchange(exchangeRateUrl, HttpMethod.GET,null , String.class);
        return response.getBody();
    }

}
