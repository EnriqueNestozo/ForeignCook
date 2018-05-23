package com.example.enriq.recetario.tareas;

import android.os.AsyncTask;

import com.example.enriq.recetario.modelo.Usuario;

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

    public SubirImagenTask(){

    }

    public SubirImagenTask(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        boolean validacion = false;
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://192.168.0.14:8080/ForeignCook/webresources/persistencia.usuarios/foto");
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");
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
            validacion= false;
            e.printStackTrace();
        } catch (IOException e) {
            validacion = false;
            e.printStackTrace();
        } catch (JSONException e) {
            validacion = false;
            e.printStackTrace();
        }finally {
            if (conn != null){
                conn.disconnect();
            }
        }

        return validacion;
    }
}
