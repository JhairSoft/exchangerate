package com.jhair.exchangerate.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table("exchange_rate")
public class ExchangeRate {

    @Id
    @Column("id_exchange_rate")
    private UUID id;
    @Column("origin_currency")
    private String originCurrency;
    @Column("final_currency")
    private String finalCurrency;
    @Column("date_to")
    private LocalDate date;
    @Column("value_to")
    private BigDecimal value;
}