package com.example.enriq.recetario.tareas;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.utilerias.Constantes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by enriq on 20/05/2018.
 */

public class EliminacionRecetaTask extends AsyncTask<Void,Void,Boolean> {
    Receta receta = null;
    List<Receta>recetas=null;
    RecyclerView.Adapter adapter;
    Usuario usuario;
    List<Usuario>usuarios;

    public EliminacionRecetaTask(Receta receta, List<Receta> recetas, RecyclerView.Adapter adapter, Usuario usuario, List<Usuario> usuarios) {
        this.receta = receta;
        this.recetas = recetas;
        this.adapter = adapter;
        this.usuario = usuario;
        this.usuarios = usuarios;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean resultado=false;
        try {
            URL url = new URL(Constantes.url + "persistencia.recetas/"+receta.getIdReceta());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestMethod("DELETE");
            connection.connect();
            InputStream input;
            if(connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST){
                input = connection.getInputStream();
                resultado=true;
            }else{
                input = connection.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String cad = bufferedReader.readLine();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if(success){

            for(int i=0;i<recetas.size();i++){
                if(recetas.get(i).getNombreReceta().equals(receta.getNombreReceta())){
                    recetas.remove(i);
                    usuarios.remove(i);
                    adapter.notifyDataSetChanged();
                }
            }

        }
    }
}
