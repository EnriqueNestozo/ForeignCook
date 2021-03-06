package com.example.enriq.recetario.utilerias;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.util.Base64;

import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.SubirImagenTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Formato {


    public static String encriptarContrasena(String contrasena){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = messageDigest.digest(contrasena.getBytes());
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < hash.length; i++) {
            stringBuilder.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuilder.toString();
    }


    public static Bitmap cambiarCalidadDeImagen(Bitmap bitsImagen, float anchoNuevo, float altoNuevo){
        int ancho = bitsImagen.getWidth();
        int alto = bitsImagen.getHeight();

        if(alto > altoNuevo || ancho > anchoNuevo){
            float escalaAncho = anchoNuevo/ancho;
            float escalaAlto = altoNuevo/alto;

            Matrix matriz = new Matrix();
            matriz.setScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitsImagen,0,0,ancho,alto,matriz,false);
        }else{
            return bitsImagen;
        }

    }

    public static String codificarImagen(Bitmap bitsImagen){

        String cadena64 = "";

        if(bitsImagen != null) {

            bitsImagen = Formato.cambiarCalidadDeImagen(bitsImagen,700,700);
            ByteArrayOutputStream arreglo = new ByteArrayOutputStream();
            bitsImagen.compress(Bitmap.CompressFormat.JPEG, 100, arreglo);
            byte[] imagenBytes = arreglo.toByteArray();

            cadena64 = Base64.encodeToString(imagenBytes,Base64.DEFAULT);

            try {
                arreglo.flush();
                arreglo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return cadena64;
    }
}
