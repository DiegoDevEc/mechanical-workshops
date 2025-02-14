package com.mechanical.workshops.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND) // Esto hará que se devuelva un 404 al lanzarla
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}