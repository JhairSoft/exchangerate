package com.jhair.exchangerate.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record ApiExchangeResponseDTO(
        boolean success,
        Long timestamp,
        String base,
        LocalDate date,
        Map<String, BigDecimal> rates) {

}
