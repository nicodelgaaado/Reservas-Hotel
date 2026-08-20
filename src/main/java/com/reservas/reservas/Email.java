/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.reservas.reservas;
import java.util.regex.Pattern;
/**
 *
 * @author Nicolas
 */
public record Email(String valor) {

    private static final Pattern PATRON_EMAIL =
            Pattern.compile("^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        if (!PATRON_EMAIL.matcher(valor).matches()) {
            throw new IllegalArgumentException(
                    "El formato del email no es válido: " + valor);
        }
    }
}
