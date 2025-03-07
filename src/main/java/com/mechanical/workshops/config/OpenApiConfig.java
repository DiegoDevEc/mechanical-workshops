package com.mechanical.workshops.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @io.swagger.v3.oas.annotations.info.Info(
        title = "Talleres Mecánicos API",
        version = "1.0",
        description = "API para Talleres Mecánicos"
    )
)
public class OpenApiConfig {
}
