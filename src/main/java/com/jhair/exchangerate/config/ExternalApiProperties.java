package com.jhair.exchangerate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "external.api")
@Data
public class ExternalApiProperties {
    private String url;
}
