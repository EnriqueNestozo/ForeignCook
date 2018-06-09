package com.example.enriq.recetario.tareas;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.enriq.recetario.actividades.InicioSesionActivity;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.utilerias.Constantes;
import com.example.enriq.recetario.utilerias.Formato;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
        boolean validacion;
        HttpURLConnection conexion = null;
        try {
            URL url = new URL(Constantes.url+"persistencia.usuarios/iniciarSesion");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.connect();
            Usuario usuario = new Usuario();
            usuario.setCorreo(correo);
            usuario.setContraseña(contraseña);
            JSONObject usuarioJSON = new JSONObject(usuario.toString());

            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(outputStream));
            escritor.write(String.valueOf(usuarioJSON));
            escritor.flush();

            InputStream input;
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                input = conn.getInputStream();
            } else {
                input = conn.getErrorStream();
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String cadena = bufferedReader.readLine();
            JSONObject respuesta = new JSONObject(cadena);
            validacion = respuesta.getString("respuesta").equals("OK");

        }catch (MalformedURLException e) {
            validacion = false;
        } catch (IOException e) {
            validacion = false;
        } catch (JSONException e) {
            validacion = false;
        } finally {
            if(conexion != null){
                conexion.disconnect();
            }
        }
        return validacion;
    }

    @Override
    protected void onPostExecute(final Boolean success){
        if(success){
            HttpURLConnection conexion = null;
            try {
                URL url = new URL(Constantes.url+"persistencia.usuarios/"+correo);
                conexion = (HttpURLConnection) url.openConnection();
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
                if(conexion != null){
                    conexion.disconnect();
                }
                actividad.cargarInicioSesion(usuario);
            }
        }else {
            Toast.makeText(actividad,"El usuario o la contraseña son erroneos", Toast.LENGTH_SHORT).show();
        }
    }
}
