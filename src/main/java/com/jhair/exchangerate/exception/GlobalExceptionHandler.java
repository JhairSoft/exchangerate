package com.jhair.exchangerate.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.jhair.exchangerate.model.dto.ErrorResponseDTO;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Mono<ErrorResponseDTO> handlerResourceNotFoundException(ResourceNotFoundException ex){
        ErrorResponseDTO error = new ErrorResponseDTO("NOT_FOUND", ex.getMessage());
        return Mono.just(error);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<Map<String, String>>> handlerWebExchangeBingException(WebExchangeBindException ex){
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Error de validación"));
        
        return Mono.just(ResponseEntity.badRequest().body(errors));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponseDTO> handlerGlobalException(Exception ex){
        ErrorResponseDTO error = new ErrorResponseDTO("INTERNAL_SERVER_ERROR", ex.getMessage());
        return Mono.just(error);
    }
    
}
