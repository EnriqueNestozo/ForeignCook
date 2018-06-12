package com.example.enriq.recetario.tareas;

import android.os.AsyncTask;
import android.widget.ListView;

import com.example.enriq.recetario.actividades.VerComentariosActivity;
import com.example.enriq.recetario.modelo.Comentario;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.utilerias.Constantes;
import com.example.enriq.recetario.utilerias.TaskCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by enriq on 11/06/2018.
 */

public class CargarComentariosTask extends AsyncTask<Void,Void,Boolean>{
    private int idReceta;
    List<Comentario> comentarios;
    TaskCallBack taskCallBack;

    public CargarComentariosTask(int idReceta, List<Comentario> comentarios, TaskCallBack taskCallBack) {
        this.idReceta = idReceta;
        this.comentarios = comentarios;
        this.taskCallBack = taskCallBack;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean cargados = false;
        comentarios.clear();
        URL url;
        try {
            url = new URL(Constantes.url + "persistencia.comentarios/comentariosRecetas/"+idReceta);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.connect();
            InputStream input;
            if(conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST){
                input = conn.getInputStream();
                cargados = true;
            }else{
                input = conn.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String cad = bufferedReader.readLine();
            JSONArray jsonRespuesta = new JSONArray(cad);
            Comentario comentarioObj;
            for(int i=0;i<jsonRespuesta.length();i++){
                JSONObject comentario = jsonRespuesta.getJSONObject(i);
                comentarioObj = new Comentario();
                comentarioObj.setNombreUsuario(comentario.getString("nombreUsuario"));
                comentarioObj.setContenido(comentario.getString("contenido"));
                Receta receta = new Receta((JSONObject) comentario.get("idReceta"));
                comentarioObj.setIdReceta(receta.getIdReceta());
                comentarioObj.setIdComentario(comentario.getInt("idComentario"));
                comentarios.add(comentarioObj);
            }

            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cargados;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean)
            taskCallBack.hecho();
    }
}
