package com.mechanical.workshops.utils;

public class IndentificationUtil {
    public static boolean isValidCedula(String cedula) {
        // Verificar que tenga 10 dígitos
        if (cedula == null || !cedula.matches("\\d{10}")) {
            return false;
        }

        // Extraer los valores clave
        int provinceCode = Integer.parseInt(cedula.substring(0, 2)); // Los dos primeros dígitos (Provincia)
        int thirdDigit = Character.getNumericValue(cedula.charAt(2)); // El tercer dígito (0-6)
        int verifierDigit = Character.getNumericValue(cedula.charAt(9)); // Último dígito (Verificador)

        // Validar el código de provincia (01 - 24)
        if (provinceCode < 1 || provinceCode > 24) {
            return false;
        }

        // Validar el tercer dígito (Debe estar entre 0 y 6)
        if (thirdDigit < 0 || thirdDigit > 6) {
            return false;
        }

        // Aplicar el algoritmo de verificación (módulo 10)
        int sum = 0;
        int[] coefficients = {2, 1, 2, 1, 2, 1, 2, 1, 2}; // Coeficientes para cada posición

        for (int i = 0; i < 9; i++) {
            int value = Character.getNumericValue(cedula.charAt(i)) * coefficients[i];
            sum += (value >= 10) ? value - 9 : value; // Si el valor es >=10, restamos 9
        }

        int calculatedVerifier = (sum % 10 == 0) ? 0 : (10 - (sum % 10)); // Obtener el dígito verificador esperado

        // Validar si el dígito calculado coincide con el último dígito
        return calculatedVerifier == verifierDigit;
    }
}
