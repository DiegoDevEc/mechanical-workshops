package com.mechanical.workshops.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SKUGenerator {
    public static String generateSKU(String nombreProducto) {
        long timestamp = System.currentTimeMillis(); // Marca de tiempo
        String input = nombreProducto + timestamp;

        return hashSHA1(input).substring(0, 8).toUpperCase();
    }

    private static String hashSHA1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
