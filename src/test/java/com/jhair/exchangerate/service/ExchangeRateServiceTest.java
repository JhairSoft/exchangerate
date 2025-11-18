package com.jhair.exchangerate.service;

import com.jhair.exchangerate.client.CurrencyApiClient;
import com.jhair.exchangerate.exception.ResourceNotFoundException;
import com.jhair.exchangerate.mapper.ExchangeRateMapper;
import com.jhair.exchangerate.model.ExchangeRate;
import com.jhair.exchangerate.model.ExternalService;
import com.jhair.exchangerate.model.dto.ExchangeRateResponseDTO;
import com.jhair.exchangerate.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private ExchangeRateMapper mapper;

    @Mock
    private CurrencyApiClient currencyApiClient;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    void findExchangeRate_WhenRateExistsInDb_ShouldReturnDtoFromDb() {
        // 1. Arrange (Preparar)
        String originCurrency = "USD";
        String finalCurrency = "PEN";
        
        ExchangeRate rateFromDb = ExchangeRate.builder()
                .id(UUID.randomUUID())
                .originCurrency(originCurrency)
                .finalCurrency(finalCurrency)
                .date(LocalDate.now())
                .value(new BigDecimal("3.75"))
                .build();

        ExchangeRateResponseDTO expectedDto = new ExchangeRateResponseDTO(
                rateFromDb.getId(),
                rateFromDb.getOriginCurrency(),
                rateFromDb.getFinalCurrency(),
                rateFromDb.getDate(),
                rateFromDb.getValue()
        );

        when(exchangeRateRepository.findTopByOriginCurrencyAndFinalCurrencyOrderByDateDesc(originCurrency, finalCurrency))
                .thenReturn(Mono.just(rateFromDb));

        when(mapper.toDto(rateFromDb)).thenReturn(expectedDto);

        // 2. Act (Actuar)
        Mono<ExchangeRateResponseDTO> resultMono = exchangeRateService.findExchangeRate(originCurrency, finalCurrency);

        // 3. Assert (Afirmar)
        StepVerifier.create(resultMono)
                .expectNext(expectedDto)
                .verifyComplete();

        verify(currencyApiClient, never()).fetchExternalRate(any(), any());
        verify(exchangeRateRepository, never()).save(any());
    }

    @Test
    void findExchangeRate_WhenRateNotInDb_ShouldFetchFromApiAndSave() {
        // 1. Arrange (Preparar)
        String originCurrency = "USD";
        String finalCurrency = "PEN";
        double rateFromApi = 3.80;

        ExternalService apiResponse = new ExternalService();
        apiResponse.setSuccess(true);
        apiResponse.setDate(LocalDate.now());
        apiResponse.setBase(originCurrency);
        apiResponse.setRates(Map.of(finalCurrency, BigDecimal.valueOf(rateFromApi)));
        
        ExchangeRate savedRate = ExchangeRate.builder()
                .id(UUID.randomUUID())
                .originCurrency(originCurrency)
                .finalCurrency(finalCurrency)
                .date(apiResponse.getDate())
                .value(BigDecimal.valueOf(rateFromApi))
                .build();

        ExchangeRateResponseDTO expectedDto = new ExchangeRateResponseDTO(
                savedRate.getId(),
                savedRate.getOriginCurrency(),
                savedRate.getFinalCurrency(),
                savedRate.getDate(),
                savedRate.getValue()
        );

        when(exchangeRateRepository.findTopByOriginCurrencyAndFinalCurrencyOrderByDateDesc(originCurrency, finalCurrency))
                .thenReturn(Mono.empty());

        when(currencyApiClient.fetchExternalRate(originCurrency, finalCurrency))
                .thenReturn(Mono.just(apiResponse));

        when(exchangeRateRepository.save(any(ExchangeRate.class))).thenReturn(Mono.just(savedRate));
        
        when(mapper.toDto(savedRate)).thenReturn(expectedDto);

        // 2. Act (Actuar)
        Mono<ExchangeRateResponseDTO> resultMono = exchangeRateService.findExchangeRate(originCurrency, finalCurrency);

        // 3. Assert (Afirmar)
        StepVerifier.create(resultMono)
                .expectNext(expectedDto)
                .verifyComplete();

        verify(currencyApiClient, times(1)).fetchExternalRate(originCurrency, finalCurrency);
        verify(exchangeRateRepository, times(1)).save(any(ExchangeRate.class));
    }

    @Test
    void findExchangeRate_WhenRateNotFoundAnywhere_ShouldThrowException() {
        // 1. Arrange (Preparar)
        String originCurrency = "USD";
        String finalCurrency = "XYZ";

        when(exchangeRateRepository.findTopByOriginCurrencyAndFinalCurrencyOrderByDateDesc(originCurrency, finalCurrency))
                .thenReturn(Mono.empty());

        when(currencyApiClient.fetchExternalRate(originCurrency, finalCurrency))
                .thenReturn(Mono.empty());

        // 2. Act (Actuar)
        Mono<ExchangeRateResponseDTO> resultMono = exchangeRateService.findExchangeRate(originCurrency, finalCurrency);

        // 3. Assert (Afirmar)
        StepVerifier.create(resultMono)
                .expectError(ResourceNotFoundException.class)
                .verify();

        // Verificaciones de Comportamiento Adicionales
        verify(currencyApiClient, times(1)).fetchExternalRate(originCurrency, finalCurrency);
        verify(exchangeRateRepository, never()).save(any(ExchangeRate.class));
    }
}