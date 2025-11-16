package com.jhair.exchangerate.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PatchExchangeRateRequestDTO(
        String originCurrency,
        String finalCurrency,
        LocalDate date,
        BigDecimal value) {

}
