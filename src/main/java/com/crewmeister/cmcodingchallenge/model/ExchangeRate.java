package com.crewmeister.cmcodingchallenge.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "exchange_rates", uniqueConstraints = @UniqueConstraint(columnNames = {"currency", "date"}))
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currency;

    private LocalDate date;
    private BigDecimal rate;

    public ExchangeRate(String currency, LocalDate date, BigDecimal rate) {
        this.currency = currency;
        this.date = date;
        this.rate = rate;
    }
}
