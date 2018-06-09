package com.example.enriq.recetario.tareas;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

public class SubirImagenTask extends AsyncTask<Void,Void,Boolean>{

    private Usuario usuario;
    private Bitmap imagen;
    private AppCompatActivity contexto;

    public SubirImagenTask(){

    }

    public SubirImagenTask(Usuario usuario, Bitmap imagen, AppCompatActivity contexto) {
        this.usuario = usuario;
        this.imagen = imagen;
        this.contexto = contexto;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        imagen = Formato.cambiarCalidadDeImagen(imagen,400,400);
        usuario.setNombreImagen(Formato.codificarImagen(imagen));

        boolean validacion = false;
        HttpURLConnection conn = null;

        if(!usuario.getNombreImagen().equals("")) {
            try {
                URL url = new URL(Constantes.url+"persistencia.usuarios/imagenesUsuarios");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
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
                String cad = bufferedReader.readLine();
                System.out.println("cad" + cad);
                validacion = true;
            } catch (MalformedURLException e) {
                validacion = false;
                e.printStackTrace();
            } catch (IOException e) {
                validacion = false;
                e.printStackTrace();
            } catch (JSONException e) {
                validacion = false;
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        return validacion;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success){
            Toast.makeText(contexto,"Se ha subido la imagen", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(contexto,"No se ha subido la imagen", Toast.LENGTH_SHORT).show();
        }
    }

}
