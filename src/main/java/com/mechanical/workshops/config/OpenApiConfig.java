package com.mechanical.workshops.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @io.swagger.v3.oas.annotations.info.Info(
        title = "Mechanical Workshops API",
        version = "1.0",
        description = "API for Mechanical Workshops"
    )
)
public class OpenApiConfig {
}
