package com.example.enriq.recetario.tareas;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImagenPerfilTask extends AsyncTask<Void,Void,Boolean>{

    private String correo;
    private CircleImageView imagen;
    private String separacion;

    public ImagenPerfilTask(){

    }

    public ImagenPerfilTask(String correo, CircleImageView imagen) {
        this.correo = correo;
        this.imagen = imagen;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String link = "http://192.168.43.126:8080/AccesoDB/webresources/alumnosWS/foto/";
        try {
            URL ourl = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) ourl.openConnection();
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.separacion);
            connection.setRequestProperty("Accept", "application/json");
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.connect();
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes("--" + this.separacion + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "foto" + "\";filename=\"" + "foto.jpg" + "\"" + "\r\n");
            outputStream.writeBytes("\r\n");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
