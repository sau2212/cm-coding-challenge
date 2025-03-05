package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.exception.ExchangeRateNotFoundException;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.model.ExchangeRateResponse;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.crewmeister.cmcodingchallenge.constant.ServiceConstant.BASE;

@RestController
@RequestMapping(BASE)
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping("/exchange-rates")
    @ApiOperation(value  = "Get all EUR-FX exchange rates at all available dates as a collection")
    public ResponseEntity<List<ExchangeRateResponse>> getExchangeRatesForCurrency() throws ExchangeRateNotFoundException {

        List<ExchangeRate> exchangeRates = exchangeRateService.getExchangeRatesForAllCurrency();
        List<ExchangeRateResponse> exchangeRateResponseList = exchangeRates.stream()
                .map(rate -> new ExchangeRateResponse(rate.getCurrency(), rate.getDate().toString(), rate.getRate()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(exchangeRateResponseList);
    }

    @GetMapping("/exchange-rate/{date}")
    @ApiOperation(value  = "Get the EUR-FX exchange rate at particular day")
    public ResponseEntity<?> getExchangeRateForCurrency(
            @Parameter(description="Currency Code in 3 letter, for example 'SEK'")
            @RequestParam(required = false) String currencyCode,
            @NotNull(message = "Date is required.")
            @Parameter(description="Date for which need to get exchange rate, formatted as yyyy-mm-dd, for example '2025-02-28'")
            @PathVariable String date) throws ExchangeRateNotFoundException {

        LocalDate localDate = LocalDate.parse(date);
        if(StringUtils.hasLength(currencyCode)) {
            ExchangeRate exchangeRate = exchangeRateService.getExchangeRateForCurrencyOnDate(currencyCode, localDate);
            return ResponseEntity.ok(new ExchangeRateResponse(exchangeRate.getCurrency(), exchangeRate.getDate().toString(), exchangeRate.getRate()));
        }
        else
        {
            List<ExchangeRateResponse> exchangeRateList = exchangeRateService.getExchangeRateByDate(localDate)
                    .stream()
                    .map(rate -> new ExchangeRateResponse(rate.getCurrency(), rate.getDate().toString(), rate.getRate()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(exchangeRateList);

        }
    }

    @GetMapping("/convert")
    @ApiOperation(value  = "Get a foreign exchange amount for a given currency converted to EUR on a particular day")
    public ResponseEntity<BigDecimal> convertToEUR(
            @NotNull(message = "Currency Code is required.")
            @Parameter(description="Currency Code in 3 letter, for example 'SEK'")
            @RequestParam String currencyCode,
            @NotNull(message = "Amount is required.")
            @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be at least 0.01.")
            @Parameter(description="Currency amount which is to be converted to Euro, for example '200'")
            @RequestParam BigDecimal amount,
            @NotNull(message = "Date is required.")
            @Parameter(description="Date for which need to get exchange rate, formatted as yyyy-mm-dd, for example '2025-02-28'")
            @RequestParam String date) throws ExchangeRateNotFoundException {

        LocalDate localDate = LocalDate.parse(date);
        BigDecimal responseAmount = exchangeRateService.convertToEUR(currencyCode, amount, localDate);
        return ResponseEntity.ok(responseAmount);
    }
}
