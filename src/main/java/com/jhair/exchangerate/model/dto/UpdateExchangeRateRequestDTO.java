package com.jhair.exchangerate.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateExchangeRateRequestDTO(

        @NotBlank(message = "La moneda de origen no puede estar vacía")
        @Size(min = 3, max = 3, message = "La moneda de origen debe tener 3 caracteres")
        String originCurrency,

        @NotBlank(message = "La moneda de destino no puede estar vacía")
        @Size(min = 3, max = 3, message = "La moneda de destino debe tener 3 caracteres")
        String finalCurrency,

        @NotNull(message = "La fecha no puede ser nula")
        LocalDate date,

        @NotNull(message = "El valor no puede ser nulo")
        @Positive(message = "EL valor debe ser un número positivo")
        BigDecimal value) {
    
}
