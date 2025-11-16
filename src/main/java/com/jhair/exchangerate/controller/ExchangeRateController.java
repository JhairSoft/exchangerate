package com.jhair.exchangerate.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jhair.exchangerate.model.dto.CreateExchangeRateRequestDTO;
import com.jhair.exchangerate.model.dto.ExchangeRateResponseDTO;
import com.jhair.exchangerate.model.dto.PatchExchangeRateRequestDTO;
import com.jhair.exchangerate.model.dto.UpdateExchangeRateRequestDTO;
import com.jhair.exchangerate.service.ExchangeRateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("exchangerate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public Flux<ExchangeRateResponseDTO> findAll(){
        return exchangeRateService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ExchangeRateResponseDTO> findById(@PathVariable UUID id){
        return exchangeRateService.findById(id);
    }

    @GetMapping("/find")
    public Mono<ExchangeRateResponseDTO> getExchangeRate(@RequestParam String originCurrency, @RequestParam String finalCurrency){
        return exchangeRateService.findExchangeRate(originCurrency, finalCurrency);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<ExchangeRateResponseDTO> create(@Valid @RequestBody CreateExchangeRateRequestDTO dto){
        return exchangeRateService.create(dto);
    }

    @PutMapping("/{id}")
    public Mono<ExchangeRateResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody UpdateExchangeRateRequestDTO dto){
        return exchangeRateService.update(id, dto);
    }

    @PatchMapping("/{id}")
    public Mono<ExchangeRateResponseDTO> patch(@PathVariable UUID id, @RequestBody PatchExchangeRateRequestDTO dto){
        return exchangeRateService.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id){
        return exchangeRateService.delete(id);
    }

}
