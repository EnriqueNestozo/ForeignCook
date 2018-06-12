package com.example.enriq.recetario.tareas;

/**
 * Created by enriq on 18/05/2018.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.example.enriq.recetario.actividades.MenuPrincipalActivity;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.utilerias.Constantes;
import com.example.enriq.recetario.utilerias.ProxyBitmap;
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

public class RecetasTask extends AsyncTask<Void,Void,Boolean> {
    private List<Receta> misRecetas;
    private List<Usuario> usuarios;
    private RecyclerView recyclerView;
    private TaskCallBack context;
    private int tipoTask;

    public RecetasTask(List<Receta> recetas, RecyclerView recyclerView, TaskCallBack context, List<Usuario>usuarios, int tipoTask) {
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
        usuarios.clear();
        URL url;
        try {
            if(tipoTask==0){
                url = new URL(Constantes.url + "persistencia.recetas/buscarPorUsuario/"+ MenuPrincipalActivity.usuario.getCorreo());
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
            System.out.println(cad);
            JSONArray jsonRespuesta = new JSONArray(cad);
            Receta receta = null;
            for(int i=0;i<jsonRespuesta.length(); i++) {
                try {
                    JSONObject jsonReceta = jsonRespuesta.getJSONObject(i);
                    JSONObject jsonUsuario = jsonReceta.getJSONObject("correo");
                    receta = new Receta(jsonReceta);
                    usuarios.add(new Usuario(jsonUsuario));
                    System.out.println("json:" + jsonReceta);
                    //RecetaListener.recetaEncontrada(new Receta(jsonReceta));
                    url = new URL(Constantes.URLImagenReceta+receta.getIdReceta()+".jpg");
                    InputStream inputStream = url.openConnection().getInputStream();
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                    Bitmap resized = Bitmap.createScaledBitmap(bmp, 300, 200, true);
                    receta.setBitmap(new ProxyBitmap(resized));
                    misRecetas.add(receta);
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
            context.hecho();
        } else {
            System.out.println("NO se pudieron cargar las recetas");
        }
    }
}
