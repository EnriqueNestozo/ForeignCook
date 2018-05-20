package com.example.enriq.recetario.tareas;

/**
 * Created by enriq on 18/05/2018.
 */

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.example.enriq.recetario.Receta;
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
import java.util.List;

public class RecetasTask extends AsyncTask<Void,Void,Boolean> {
    private List<Receta> misRecetas;
    RecyclerView recyclerView;

    public RecetasTask(List<Receta> recetas,RecyclerView recyclerView) {
        this.misRecetas = recetas;
        this.recyclerView = recyclerView;
    }



    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean resultado = false;
        misRecetas.clear();
        try {

            URL url = new URL(Constantes.url() + "persistencia.recetas/buscarPorUsuario/enrique@gmail.com");
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
                    System.out.println("json:" + jsonReceta);
                    misRecetas.add(new Receta(jsonReceta));

                    //RecetaListener.recetaEncontrada(new Receta(jsonReceta));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            resultado = true;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            System.out.println("misrecetas" + misRecetas);
            this.recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            System.out.println("NO se pudieron cargar las recetas");
        }
    }
}
