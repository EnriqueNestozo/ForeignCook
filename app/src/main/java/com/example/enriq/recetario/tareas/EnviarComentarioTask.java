package com.example.enriq.recetario.tareas;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.enriq.recetario.actividades.VerComentariosActivity;
import com.example.enriq.recetario.modelo.Comentario;
import com.example.enriq.recetario.utilerias.Constantes;
import com.example.enriq.recetario.utilerias.TaskCallBack;

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
import java.util.List;

/**
 * Created by enriq on 11/06/2018.
 */

public class EnviarComentarioTask extends AsyncTask<Void,Void,Boolean> {
    private Comentario comentario;
    private TaskCallBack context;
    private int idReceta;
    private List<Comentario> comentarios;

    public EnviarComentarioTask(int idReceta,List<Comentario>comentarios,Comentario comentario, TaskCallBack context) {
        this.comentario = comentario;
        this.context = context;
        this.idReceta = idReceta;
        this.comentarios = comentarios;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean respuesta = false;
        URL url;
        try {
            url = new URL(Constantes.url+"persistencia.comentarios/"+comentario.getIdReceta());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestMethod("POST");
            conn.connect();
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("contenido",comentario.getContenido());
            jsonObject.accumulate("nombreUsuario",comentario.getNombreUsuario());
            jsonObject.accumulate("idReceta",comentario.getIdReceta());
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(outputStream));
            escritor.write(String.valueOf(jsonObject));
            escritor.flush();
            InputStream input;
            if(conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST){
                input = conn.getInputStream();
                respuesta = true;
            }else{
                input = conn.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String cad = bufferedReader.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if(success){
            new CargarComentariosTask(idReceta,comentarios, context).execute();
        }else {
            Toast.makeText((Context) context, "No se pudo enviar el comentario", Toast.LENGTH_SHORT).show();
        }
    }
}
