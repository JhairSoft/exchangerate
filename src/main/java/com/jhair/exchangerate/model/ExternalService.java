package com.jhair.exchangerate.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ExternalService {

    private boolean success;
    private Long timestamp;
    private String base;
    private LocalDate date;
    private Map<String, BigDecimal> rates;

}
