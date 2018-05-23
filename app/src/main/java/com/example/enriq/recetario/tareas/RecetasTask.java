package com.example.enriq.recetario.tareas;

/**
 * Created by enriq on 18/05/2018.
 */

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;

import com.example.enriq.recetario.MainActivity;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.utilerias.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecetasTask extends AsyncTask<Void,Void,Boolean> {
    private List<Receta> misRecetas;
    private List<Usuario> usuarios;
    private RecyclerView recyclerView;
    private MainActivity context;
    private int tipoTask;

    public RecetasTask(List<Receta> recetas, RecyclerView recyclerView, MainActivity context, List<Usuario>usuarios,int tipoTask) {
        this.misRecetas = recetas;
        this.recyclerView = recyclerView;
        this.usuarios = usuarios;
        this.context = context;
        this.tipoTask = tipoTask;
    }



    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean resultado = false;
        misRecetas.clear();
        URL url;
        try {
            if(tipoTask==0){
                url = new URL(Constantes.url + "persistencia.recetas/enrique@gmail.com");
            }else{
                url = new URL(Constantes.url + "persistencia.recetas");
            }

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.connect();
            InputStream input;
            if(conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST){
                input = conn.getInputStream();
            }else{
                input = conn.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String cad = bufferedReader.readLine();

            JSONArray jsonRespuesta = new JSONArray(cad);
            for(int i=0;i<jsonRespuesta.length(); i++) {
                try {
                    JSONObject jsonReceta = jsonRespuesta.getJSONObject(i);
                    JSONObject jsonUsuario = jsonReceta.getJSONObject("correo");
                    System.out.println("json:" + jsonReceta);
                    misRecetas.add(new Receta(jsonReceta));
                    usuarios.add(new Usuario(jsonUsuario));
                    //RecetaListener.recetaEncontrada(new Receta(jsonReceta));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            resultado = true;
            conn.disconnect();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            context.setUsuarios(usuarios);
            int likesTotales = 0;
            for(int i=0;i<misRecetas.size();i++){
                likesTotales=likesTotales+misRecetas.get(i).getLikes();
            }
            context.setLikes(likesTotales);
            this.recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            System.out.println("NO se pudieron cargar las recetas");
        }
    }
}
