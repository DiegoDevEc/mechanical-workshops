package com.mechanical.workshops.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "whatsapp.messages")
@Getter
@Setter
public class WhatsAppMessagesConfig {
    private String reservaExitosa;
}
