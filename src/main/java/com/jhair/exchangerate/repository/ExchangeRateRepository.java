package com.jhair.exchangerate.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.jhair.exchangerate.model.ExchangeRate;

import reactor.core.publisher.Mono;

@Repository
public interface ExchangeRateRepository extends R2dbcRepository<ExchangeRate, UUID>{

    public Mono<ExchangeRate> findTopByOriginCurrencyAndFinalCurrencyOrderByDateDesc(String originCurrency, String finalCurrency);
}
