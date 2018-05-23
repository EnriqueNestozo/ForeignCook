package com.example.enriq.recetario.tareas;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.example.enriq.recetario.actividades.RegistrarUsuarioActivity;
import com.example.enriq.recetario.modelo.Usuario;
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

public class RegistroUsuarioTask extends AsyncTask<Void,Void,Boolean>{

    private Usuario usuario;
    private RegistrarUsuarioActivity contexto;
    private Bitmap archivo;

    public RegistroUsuarioTask() {

    }

    public RegistroUsuarioTask(Usuario usuario) {
        this.usuario = usuario;
    }

    public RegistroUsuarioTask(Usuario usuario, RegistrarUsuarioActivity contexto,Bitmap archivo) {
        this.usuario = usuario;
        this.contexto = contexto;
        this.archivo = archivo;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean validacion = false;

        try {
            URL url = new URL("http://"+ Formato.DIRECCION+":8080/ForeignCook/webresources/persistencia.usuarios");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
            String cadena = bufferedReader.readLine();
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
        }

        return validacion;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success){
            SubirImagenPerfilTask task = new SubirImagenPerfilTask(usuario,archivo);

            task.execute();

            AlertDialog.Builder dialogo = new AlertDialog.Builder(contexto);
            dialogo.setTitle("Mensaje");
            dialogo.setMessage("Se ha guardado el usuario").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    contexto.finish();
                }
            });
            dialogo.show();
        }else{
            System.out.println("pase");
        }
    }
}
