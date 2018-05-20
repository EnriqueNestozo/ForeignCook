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

import com.example.enriq.recetario.actividades.InicioSesionActivity;
import com.example.enriq.recetario.tareas.RecetasTask;
import com.example.enriq.recetario.utilerias.Constantes;

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

    public void editarPerfil(View view){
        Intent intent = new Intent(this, InicioSesionActivity.class);
        startActivity(intent);
    }


}
