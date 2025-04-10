package com.mechanical.workshops.constants;

import java.time.Month;
import java.util.Map;

public class Constants {

    private Constants() {
    }

    public static final String USER_NOT_FOUND = "El usuario: %s no existe";
    public static final String USER_UPDATED = "El usuario: %s se actualizo correctamente";
    public static final String USER_CREATED = "El usuario: %s se creo correctamente";
    public static final String USER_DELETED = "El usuario: %s se elimino correctamente";

    public static final String ENTITY_NOT_FOUND = "El %s: %s no existe";
    public static final String ENTITY_UPDATED = "El %s: %s se actualizo correctamente";
    public static final String ENTITY_CREATED = "El %s: %s se creo correctamente";
    public static final String ENTITY_DELETED = "El %s: %s se elimino correctamente";
    public static final String ENTITY_CANCELED = "El %s: %s se cancelo correctamente";

    public static final String ENTITY_ALREADY_EXISTS = "La %s: con el nombre: %s ya existe";
    public static final String ENTITY_ALREADY_EXISTS_BY_SKU = "El %s: con el SKU: %s ya existe";

    public static final String SERVICE = "Servicio";
    public static final String VEHICLE = "Vehículo";
    public static final String CATEGORY = "Categoría";
    public static final String PRODUCT = "Producto";
    public static final String APPOINTMENT = "Cita";
    public static final String ATTENDANCE = "Cita - Atención";
    public static final String AVAILABLE_APPOINTMENT = "Cita disponible";
    public static final String USER = "Usuario";

    public static final String USER_PASSWORD_CHANGED = "La contraseña del usuario: %s se actualizo correctamente";

    public static final String IDENTIFICATION_FORMAT_VALID = "La cédula: %s es correcta";
    public static final String IDENTIFICATION_FORMAT_INVALID = "La cédula: %s es incorrecta";

    public static final String USER_ALREADY_EXISTS = "El %s ya se encuentra registrado por otro usuario";
    public static final String USER_VALID = "El %s es válido";

    public static final String PHONE = "Teléfono";
    public static final String EMAIL = "Correo electrónico";
    public static final String USERNAME = "Nombre de usuario";
    public static final String IDENTIFICATION = "Identificación";

    public static final String ERROR_TOKEN = "El token no es válido";

    private static final Map<String, Month> SPANISH_MONTHS = Map.ofEntries(
            Map.entry("enero", Month.JANUARY),
            Map.entry("febrero", Month.FEBRUARY),
            Map.entry("marzo", Month.MARCH),
            Map.entry("abril", Month.APRIL),
            Map.entry("mayo", Month.MAY),
            Map.entry("junio", Month.JUNE),
            Map.entry("julio", Month.JULY),
            Map.entry("agosto", Month.AUGUST),
            Map.entry("septiembre", Month.SEPTEMBER),
            Map.entry("octubre", Month.OCTOBER),
            Map.entry("noviembre", Month.NOVEMBER),
            Map.entry("diciembre", Month.DECEMBER)
    );
}
