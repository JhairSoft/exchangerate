package com.jhair.exchangerate.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExchangeRateResponseDTO(
        UUID id,
        String originCurrency,
        String finalCurrency,
        LocalDate date,
        BigDecimal value) {

}
