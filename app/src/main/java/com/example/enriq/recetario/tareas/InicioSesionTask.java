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
            conexion = (HttpURLConnection)url.openConnection();
            conexion.setRequestProperty("Content-Type","application/json");
            conexion.setRequestProperty("Accept","application/json");
            conexion.setDoInput(true);
            conexion.setDoOutput(true);
            conexion.setRequestMethod("POST");
            conexion.connect();

            Usuario usuario = new Usuario();
            usuario.setCorreo(correo);
            usuario.setContraseña(contraseña);
            JSONObject usuarioJSON = new JSONObject(usuario.toString());

            OutputStream outputStream = conexion.getOutputStream();
            BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(outputStream));
            escritor.write(String.valueOf(usuarioJSON));
            escritor.flush();

            InputStream input;
            if (conexion.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                input = conexion.getInputStream();
            } else {
                input = conexion.getErrorStream();
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String cadena = bufferedReader.readLine();
            System.out.printf("cad"+cadena);
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
            new UsuarioTask(actividad,correo).execute();
        }else {
            Toast.makeText(actividad,"El usuario o la contraseña son erroneos", Toast.LENGTH_SHORT).show();
        }
    }
}
