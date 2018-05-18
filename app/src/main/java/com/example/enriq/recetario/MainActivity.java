package com.example.enriq.recetario;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView myRecycleView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    FloatingActionButton floatingActionButton;
    List<Receta> recetas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        myRecycleView = findViewById(R.id.my_recycler_view);
        myRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        myRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(recetas);

        myRecycleView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new RecetasTask(recetas,myRecycleView).execute();
    }

    public void publicarReceta(View view){
        Intent intent = new Intent(this,PosteoRecetaActivity.class);
        startActivity(intent);
    }
}

class RecetasTask extends AsyncTask<Void,Void,Boolean>{
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
            URL url = new URL("http://192.168.0.105:11866/ForeignCook2/webresources/persistencia.recetas/buscarPorUsuario/enrique@gmail.com");
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
