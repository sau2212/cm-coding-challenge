package com.crewmeister.cmcodingchallenge.exception;

public class ExchangeRateNotFoundException extends Exception{

    public ExchangeRateNotFoundException(String message) {
        super(message);
    }

    public ExchangeRateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExchangeRateNotFoundException(Throwable cause) {
        super(cause);
    }
}