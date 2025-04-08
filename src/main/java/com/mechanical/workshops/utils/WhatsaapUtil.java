package com.mechanical.workshops.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class WhatsaapUtil {

    private static final String ACCOUNT_SID = "AC82a201ca2cac8d4ab1c55ac651a819d9";
    private static final String AUTH_TOKEN = "5474baa8f6232bfd802f2cf832ac4124";
    private static final String TWILIO_PREFIX = "whatsapp:+593";
    private static final String WHATSAPP_NUMBER = "whatsapp:+14155238886";

    public static void sendMsjWhatsapp(String phoneSender, String messageUser) {
        String numberUser = TWILIO_PREFIX + phoneSender;
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new PhoneNumber(numberUser),
                        new PhoneNumber(WHATSAPP_NUMBER),
                        messageUser)
                .create();

        System.out.println(message.getSid());
    }
}
