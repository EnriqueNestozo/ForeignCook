package com.example.enriq.recetario.tareas;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.utilerias.Constantes;

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

public class EditarUsuarioTask extends AsyncTask<Void,Void,Boolean> {

    private Usuario usuario;
    private AppCompatActivity contexto;
    private Bitmap archivo;

    public EditarUsuarioTask(){

    }

    public EditarUsuarioTask(Usuario usuario, AppCompatActivity contexto, Bitmap archivo) {
        this.usuario = usuario;
        this.contexto = contexto;
        this.archivo = archivo;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean validacion;
        try {
            URL url = new URL(Constantes.url+"persistencia.usuarios/"+usuario.getCorreo());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.connect();

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
        } catch (MalformedURLException e) {
            validacion= false;
            e.printStackTrace();
        } catch (IOException e) {
            validacion = false;
            e.printStackTrace();
        } catch (JSONException e) {
            validacion = false;
            e.printStackTrace();
        }
        return validacion;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success){
            if(archivo != null){
                new SubirImagenTask(usuario,archivo,contexto).execute();
            }
            Toast.makeText(contexto,"Se ha editado tu perfil", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(contexto,"Ha ocurrido un error al editar el perfil", Toast.LENGTH_SHORT).show();
    }
}
