package com.jhair.exchangerate.mapper;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jhair.exchangerate.model.ExchangeRate;
import com.jhair.exchangerate.model.dto.ApiExchangeResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiExchangeMapper {
    
    @Mapping(source = "dto.date", target = "date")
    @Mapping(source = "originCurrency", target = "originCurrency")
    @Mapping(source = "finalCurrency", target = "finalCurrency")
    @Mapping(source = "value", target = "value")
    @Mapping(target = "id", ignore = true)
    ExchangeRate toEntity(ApiExchangeResponseDTO dto, String originCurrency, String finalCurrency, BigDecimal value);
}
