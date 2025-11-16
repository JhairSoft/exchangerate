package com.jhair.exchangerate.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.jhair.exchangerate.model.ExchangeRate;
import com.jhair.exchangerate.model.dto.CreateExchangeRateRequestDTO;
import com.jhair.exchangerate.model.dto.ExchangeRateResponseDTO;
import com.jhair.exchangerate.model.dto.PatchExchangeRateRequestDTO;
import com.jhair.exchangerate.model.dto.UpdateExchangeRateRequestDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExchangeRateMapper {
    
    ExchangeRateResponseDTO toDto(ExchangeRate entity);

    ExchangeRate toEntity(CreateExchangeRateRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(UpdateExchangeRateRequestDTO dto, @MappingTarget ExchangeRate entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchFromDto(PatchExchangeRateRequestDTO dto, @MappingTarget ExchangeRate entity);
}
