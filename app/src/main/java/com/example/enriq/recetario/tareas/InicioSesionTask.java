package com.example.enriq.recetario.tareas;

import android.os.AsyncTask;

import com.example.enriq.recetario.actividades.InicioSesionActivity;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.utilerias.Formato;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InicioSesionTask extends AsyncTask<Void,Void,Boolean>{

    private String correo;
    private String contraseña;
    private InicioSesionActivity actividad;
    private Usuario usuario;

    public InicioSesionTask(String correo, String contraseña, InicioSesionActivity actividad) {
        this.correo = correo;
        this.contraseña = Formato.encriptarContrasena(contraseña);
        this.actividad = actividad;
    }

    public InicioSesionTask(){

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            String cadenaURL = "http://192.168.0.14:8080/Foreign/webresources/persistencia.usuarios/iniciarSesion/"+correo+"/"+contraseña;
            URL url = new URL(cadenaURL);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setRequestProperty("Accept", "text/plain");

            InputStream entrada;

            if (conexion.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                entrada = conexion.getInputStream();
            } else {
                entrada = conexion.getErrorStream();
            }

            BufferedReader lector = new BufferedReader(new InputStreamReader(entrada));
            String cadena = lector.readLine();

            return cadena.equals("true");

        }catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success){
        if(success){
            try {
                URL url = new URL("http://192.168.0.14:8080/Foreign/webresources/persistencia.usuarios/"+correo);
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestProperty("Content-Type", "application/json");
                conexion.setRequestProperty("Accept", "application/json");

                InputStream entrada;

                if(conexion.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST){
                    entrada = conexion.getInputStream();
                }else {
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


            } catch (IOException ex) {
                System.out.println("Error");
            }finally {
                actividad.cargarInicioSesion(usuario);
            }
        }
    }
}
