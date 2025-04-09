package com.mechanical.workshops.service;

import com.mechanical.workshops.utils.WhatsaapUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WhatsappServiceImpl implements WhatsappService {
    @Override
    public void send(String number, String message, Map<String, String> parameters) {
        String messageUser = generateMessage(message, parameters);
       // WhatsaapUtil.sendMsjWhatsapp(number, messageUser);
    }

    private String generateMessage(String message, Map<String, String> parameters) {
        return String.format(message, parameters.get("name"), parameters.get("date"), parameters.get("hour"));
    }
}
