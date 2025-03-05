package com.crewmeister.cmcodingchallenge.repository;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    ExchangeRate findByCurrencyAndDate(String currency, LocalDate date);

    List<ExchangeRate> findByDate(LocalDate date);

    boolean existsByCurrencyAndDate(String currency, LocalDate date);

}
