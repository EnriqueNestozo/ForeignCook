package com.example.enriq.recetario.tareas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.enriq.recetario.actividades.InicioSesionActivity;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.utilerias.Constantes;
import com.example.enriq.recetario.utilerias.ProxyBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UsuarioTask extends AsyncTask<Void,Void,Boolean> {

    private InicioSesionActivity actividad;
    private String correo;
    private Usuario usuario;

    public UsuarioTask(InicioSesionActivity actividad, String correo) {
        this.actividad = actividad;
        this.correo = correo;
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        HttpURLConnection conexion = null;

        try {
            URL url = new URL(Constantes.url + "persistencia.usuarios/" + correo);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setRequestProperty("Accept", "application/json");
            conexion.connect();

            InputStream entrada;

            if (conexion.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                entrada = conexion.getInputStream();
            } else {
                entrada = conexion.getErrorStream();
            }

            BufferedReader lector = new BufferedReader(new InputStreamReader(entrada));
            String cadena = lector.readLine();


            try {
                JSONObject usuarioJSON = new JSONObject(cadena);
                usuario = new Usuario(usuarioJSON);
            } catch (JSONException e) {
                usuario = null;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                conexion.disconnect();
            }
        }

        try{
            URL url = new URL(Constantes.URLImagenPerfil+usuario.getNombreImagen());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ProxyBitmap proxyBitmap = new ProxyBitmap(getResizedBitmap(bmp,100,100));
            usuario.setProxyBitmap(proxyBitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        actividad.cargarInicioSesion(usuario);
    }
}
