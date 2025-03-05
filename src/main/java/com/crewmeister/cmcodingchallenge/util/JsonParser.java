package com.crewmeister.cmcodingchallenge.util;

import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.crewmeister.cmcodingchallenge.constant.ServiceConstant.*;

@Component
public class JsonParser {
    private final Logger log = LoggerFactory.getLogger(JsonParser.class);

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    public List<Currency> parseCurrencyJson(String currencyJson) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(currencyJson);

        // Extract currency data from the "codes" list
        JsonNode codesNode = rootNode.path(DATA).path(CODE_LISTS).get(0).path(CODES);

        List<Currency> currencies = new ArrayList<>();
        for (JsonNode currencyNode : codesNode) {
            String code = currencyNode.get(ID).asText();
            String name = currencyNode.path(NAMES).path(EN).asText();
            currencies.add(new Currency(code, name));
        }

        return currencies;
    }

    private JsonNode parseJsonResponse(String responseJson) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseJson);
    }

    //Method to extract currencies from the JSON data
    private List<String> extractCurrencies(JsonNode currenciesNode) {
        List<String> currencies = new ArrayList<>();
        for (JsonNode currency : currenciesNode) {
            currencies.add(currency.get(ID).asText());
        }
        return currencies;
    }

    //Method to extract dates from the JSON data
    private List<String> extractDates(JsonNode datesNode) {
        List<String> dates = new ArrayList<>();
        for (JsonNode date : datesNode) {
            dates.add(date.get(ID).asText());
        }
        return dates;
    }

    //Method to extract series node (exchange rates data)
    private JsonNode extractSeriesNode(JsonNode rootNode) {
        return rootNode.path(DATA).path(DATASETS).get(0).path(SERIES);
    }

    // 5. Method to extract and save the exchange rates in DB
    private void parseExchangeRates(JsonNode seriesNode, List<String> currencies, List<String> dates) {
        for (Iterator<String> it = seriesNode.fieldNames(); it.hasNext(); ) {
            String key = it.next();

            // Getting the rates array
            JsonNode observations = seriesNode.get(key).path(OBSERVATIONS);

            // The second field in the "0:3:0:0:0:0" pattern maps to Currency
            int currencyIndex = Integer.parseInt(key.split(":")[1]);
            String currency = currencies.get(currencyIndex);

            // observations have index which will map to date like "0:1:0:0:0:0"
            for (Iterator<String> obsIt = observations.fieldNames(); obsIt.hasNext(); ) {
                String dateIndex = obsIt.next();
                String date = dates.get(Integer.parseInt(dateIndex));
                try {
                    BigDecimal exchangeRate = new BigDecimal(observations.get(dateIndex).get(0).asText());
                    saveExchangeRateInDB(currency, date, exchangeRate);
                }
                catch(Exception e){
                    //Ignoring null values
                }


            }
        }
    }

    private void saveExchangeRateInDB(String currency, String date, BigDecimal exchangeRate)
    {
        boolean exists = exchangeRateRepository.existsByCurrencyAndDate(currency, LocalDate.parse(date));

        if (exists) {
            log.info("Exchange rate for {} on {} already exists.", currency, date);
        } else {
            exchangeRateRepository.save(new ExchangeRate(currency, LocalDate.parse(date), exchangeRate));
        }
    }


    public void parseExchangeRate(String responseJson) throws Exception {
        JsonNode rootNode = parseJsonResponse(responseJson);

        // Extracting currencies and dates from the JSON
        JsonNode currenciesNode = rootNode.path(DATA).path(STRUCTURE).path(DIMENSIONS).path(SERIES).get(1).path(VALUES);
        JsonNode datesNode = rootNode.path(DATA).path(STRUCTURE).path(DIMENSIONS).path(OBSERVATION).get(0).path(VALUES);

        List<String> currencies = extractCurrencies(currenciesNode);
        List<String> dates = extractDates(datesNode);

        // Extract the series node (exchange rate data)
        JsonNode seriesNode = extractSeriesNode(rootNode);

        // Parsing exchange rates and saving to the database
        parseExchangeRates(seriesNode, currencies, dates);
    }
}
