package com.example.enriq.recetario.tareas;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.example.enriq.recetario.actividades.PosteoRecetaActivity;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.modelo.Usuario;
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

/**
 * Created by enriq on 19/05/2018.
 */

public class PosteoTask extends AsyncTask<Void,Void,Boolean> {
    boolean resultado=false;
    Receta receta;
    TaskCallBack context;
    AlertDialog.Builder builder;

    public PosteoTask(Receta receta, TaskCallBack context) {
        this.receta = receta;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String strUrl = Constantes.url+ "persistencia.recetas/"+receta.getCorreo();
        System.out.println(strUrl);
        String metodoEnvio = "POST";
        if(receta.getIdReceta()>0){
            metodoEnvio="PUT";
            System.out.println("PUT");
        }
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod(metodoEnvio);
            conn.connect();
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("idReceta",receta.getIdReceta());
            jsonObject.accumulate("nombreReceta",receta.getNombreReceta());
            jsonObject.accumulate("descripcion",receta.getDescripcion());
            jsonObject.accumulate("ingredientes",receta.getIngredientes());
            jsonObject.accumulate("procedimiento",receta.getPasos());
            jsonObject.accumulate("categoria",receta.getCategoria());
            jsonObject.accumulate("likes",receta.getLikes());
            jsonObject.accumulate("linkVideo",receta.getLinkVideo());
            jsonObject.accumulate("correo",receta.getCorreo());
            //nombreImagen

            System.out.println(jsonObject);
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(outputStream));
            escritor.write(String.valueOf(jsonObject));
            escritor.flush();

            InputStream input;
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                input = conn.getInputStream();
                resultado = true;
            } else {
                input = conn.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String cad = bufferedReader.readLine();
            JSONObject salida = new JSONObject(cad);
            receta.setIdReceta(salida.getInt("idReceta"));

            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(resultado);
        return resultado;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            new SubirImagenRecetaTask(context,receta,receta.getImagenReceta()).execute();
        } else {

        }
    }
}
