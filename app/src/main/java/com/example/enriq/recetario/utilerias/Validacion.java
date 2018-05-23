package com.example.enriq.recetario.utilerias;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacion {

    private static final String PATRON_CORREO = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PATRON_CONTRASEÑA = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,100}$";
    private static final String PATRON_VIDEOURL = "https://www.youtube.com/watch?";
    public static boolean validarFormatoCorreo(String email) {
        Pattern patron = Pattern.compile(PATRON_CORREO);
        Matcher concordancia = patron.matcher(email);
        return concordancia.matches();
    }

    public static boolean validarFormatoContraseña(String contraseña) {
        Pattern patron = Pattern.compile(PATRON_CONTRASEÑA);
        Matcher concordancia = patron.matcher(contraseña);
        return concordancia.matches();
    }
}