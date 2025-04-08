package com.mechanical.workshops.service;

import java.util.Map;

public interface WhatsappService {
    void send(String number, String message, Map<String, String> parameters);
}
