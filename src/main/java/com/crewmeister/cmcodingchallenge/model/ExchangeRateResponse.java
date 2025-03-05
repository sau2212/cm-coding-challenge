package com.crewmeister.cmcodingchallenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeRateResponse {
    private String currencyCode;
    private String date;
    private BigDecimal rate;
}
