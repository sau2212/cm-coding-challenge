package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.service.CurrencyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.crewmeister.cmcodingchallenge.constant.ServiceConstant.BASE;

@RestController
@RequestMapping(BASE)
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;

   @GetMapping("/currencies")
   @ApiOperation(value  = "Get a list of all available currencies")
    public ResponseEntity<List<Currency>> getCurrencies() {

       List<Currency> currencyList = currencyService.getAllCurrencies();
        return ResponseEntity.ok(currencyList);
    }
}
