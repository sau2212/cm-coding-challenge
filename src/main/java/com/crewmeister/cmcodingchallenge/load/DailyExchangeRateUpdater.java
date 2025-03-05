package com.crewmeister.cmcodingchallenge.load;

import com.crewmeister.cmcodingchallenge.util.BundesbankClient;
import com.crewmeister.cmcodingchallenge.util.JsonParser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.crewmeister.cmcodingchallenge.constant.ServiceConstant.*;

@Component
public class DailyExchangeRateUpdater {

    /*Application load exchange Rate from bundesbank at startup
    * This job will help in keeping the DB up to date, by daily updating the data for each day.*/
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DailyExchangeRateUpdater.class);

    @Autowired
    JsonParser jsonParser;

    @Autowired
    BundesbankClient bundesbankClientUtil;

    @Value("${bundesbank.exchangeRates.url}")
    private String exchangeRateUrl;

    @Value("${daily.exchangeRate.job.enabled}")
    private boolean dailyExchangeRateJobEnabled;

    @Scheduled(cron = "0 10 0 * * ?")
    private void updateExchangeRateInDB()
    {
        if(!dailyExchangeRateJobEnabled)
            return;
        String exchangeRateUrlWithDate = getExchangeRateURL();

        logger.info("Loading updated data for Exchange Rate from bundesbank");
        String exchangeRateData = bundesbankClientUtil.getExchangeRateDataAsString(exchangeRateUrlWithDate);

        try {
            jsonParser.parseExchangeRate(exchangeRateData);
            logger.info("Exchange Rates are loaded from bundesbank Successfully!!!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String getExchangeRateURL()
    {
        String prevDayDate = LocalDate.now().minusDays(1).toString();
        String exchangeRateUrlWithDate = exchangeRateUrl + PARAMETER_AND +
                START_PERIOD+ prevDayDate + PARAMETER_AND + END_PERIOD + prevDayDate;

        logger.info("URL to get daily Exchange rate update : " + exchangeRateUrlWithDate);
        return exchangeRateUrlWithDate;
    }
}
