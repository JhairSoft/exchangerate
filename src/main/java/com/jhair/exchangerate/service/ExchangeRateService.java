package com.jhair.exchangerate.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jhair.exchangerate.client.CurrencyApiClient;
import com.jhair.exchangerate.mapper.ExchangeRateMapper;
import com.jhair.exchangerate.model.ExchangeRate;
import com.jhair.exchangerate.model.dto.CreateExchangeRateRequestDTO;
import com.jhair.exchangerate.model.dto.ExchangeRateResponseDTO;
import com.jhair.exchangerate.model.dto.PatchExchangeRateRequestDTO;
import com.jhair.exchangerate.model.dto.UpdateExchangeRateRequestDTO;
import com.jhair.exchangerate.repository.ExchangeRateRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper mapper;
    private final CurrencyApiClient currencyApiClient;

    public Flux<ExchangeRateResponseDTO> findAll(){
        return exchangeRateRepository.findAll()
                .map(mapper::toDto);
    }

    public Mono<ExchangeRateResponseDTO> findById(UUID id){
        return exchangeRateRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró el Id : " + id)))
                .map(mapper::toDto);
    }

    public Mono<ExchangeRateResponseDTO> findExchangeRate(String originCurrency, String finalCurrency){
        return exchangeRateRepository.findTopByOriginCurrencyAndFinalCurrencyOrderByDateDesc(originCurrency, finalCurrency)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.defer(() -> findAndSaveExchangeRate(originCurrency, finalCurrency)))
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró el cambio de " + originCurrency + " a " + finalCurrency)));
    }

    public Mono<ExchangeRateResponseDTO> create(CreateExchangeRateRequestDTO dto){
        ExchangeRate entity = mapper.toEntity(dto);
        return exchangeRateRepository.save(entity)
                .map(mapper::toDto);
    }

    public Mono<ExchangeRateResponseDTO> update(UUID id, UpdateExchangeRateRequestDTO dto){
        return exchangeRateRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró el Id : " + id)))
                .flatMap( foundEntity -> {
                    mapper.updateFromDto(dto, foundEntity);
                    return exchangeRateRepository.save(foundEntity);
                })
                .map(mapper::toDto);
    }

    public Mono<ExchangeRateResponseDTO> patch(UUID id, PatchExchangeRateRequestDTO dto){
        return exchangeRateRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró el Id : " + id)))
                .flatMap( foundEntity -> {
                    mapper.patchFromDto(dto, foundEntity);
                    return exchangeRateRepository.save(foundEntity);
                })
                .map(mapper::toDto);
    }

    public Mono<Void> delete(UUID id){
        return exchangeRateRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró el Id : " + id)))
                .flatMap(exchangeRateRepository::delete);
    }

    public Mono<ExchangeRateResponseDTO> findAndSaveExchangeRate(String originCurrency, String finalCurrency){
        return currencyApiClient.fetchExternalRate(originCurrency, finalCurrency)
                .flatMap(externalService ->
                            Mono.justOrEmpty(externalService.getRates().get(finalCurrency))
                                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró la tasa para la moneda " + finalCurrency)))
                                .flatMap(foundRate -> {
                                            ExchangeRate newRate = ExchangeRate.builder()
                                                .originCurrency(originCurrency)
                                                .finalCurrency(finalCurrency)
                                                .date(externalService.getDate())
                                                .value(foundRate)
                                                .build();

                                    return exchangeRateRepository.save(newRate);
                                })
                )
                .map(mapper::toDto);
    }

}