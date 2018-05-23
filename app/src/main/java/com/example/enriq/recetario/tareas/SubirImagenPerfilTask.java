package com.example.enriq.recetario.tareas;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.utilerias.Formato;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SubirImagenPerfilTask extends AsyncTask<Void,Void,Boolean>{
    private String separacion = "**-------****----***-------**";
    private Usuario usuario;
    private Bitmap bitsImagen;

    public SubirImagenPerfilTask() {

    }

    public SubirImagenPerfilTask(Usuario usuario, Bitmap bitsImagen) {
        this.usuario = usuario;
        this.bitsImagen = bitsImagen;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean validacion;
        try {
            String metodoEnvio = "POST";
            URL ourl = new URL("http://"+ Formato.DIRECCION+":8080/ForeignCook/webresources/persistencia.usuarios/fotos/" + usuario.getCorreo());
            HttpURLConnection conn = (HttpURLConnection) ourl.openConnection();
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.separacion);
            conn.setRequestProperty("Accept", "application/json");
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(metodoEnvio);
            conn.connect();

            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes("--" + this.separacion + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                    "foto" + "\";filename=\"" +
                    "foto.jpg" + "\"" + "\r\n");
            outputStream.writeBytes("\r\n");

            ByteArrayOutputStream arreglo = new ByteArrayOutputStream();
            bitsImagen.compress(Bitmap.CompressFormat.JPEG, 100, arreglo);
            byte[] archivo = arreglo.toByteArray();
            arreglo.flush();
            arreglo.close();

            int tamaño = archivo.length;
            outputStream.write(archivo, 0, tamaño);

            outputStream.writeBytes("\r\n");
            outputStream.writeBytes("--" + separacion +
                    "--" + "\r\n");

            outputStream.flush();
            outputStream.close();

            InputStream inputStream;
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String cad = bufferedReader.readLine();
            System.out.println(cad);
        } catch (MalformedURLException e) {
            System.err.println(e);
            return false;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        //return null;
        return true;
    }
}
