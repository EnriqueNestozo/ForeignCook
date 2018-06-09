package com.example.enriq.recetario.actividades;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.enriq.recetario.utilerias.MyAdapter;
import com.example.enriq.recetario.R;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.RecetasTask;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.utilerias.TaskCallBack;

import java.util.ArrayList;
import java.util.List;

public class MisRecetasActivity extends AppCompatActivity implements TaskCallBack{
    private RecyclerView myRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton floatingActionButton;
    private List<Receta> recetas = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private TextView textViewlikesTotales;
    private TextView numeroRecetas;
    private Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.perfil){
            System.out.println("Desplegar perfil");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        myRecycleView = findViewById(R.id.my_recycler_view);
        textViewlikesTotales = findViewById(R.id.textViewLikes);
        numeroRecetas = findViewById(R.id.textViewNumeroRecetas);
        myRecycleView.setHasFixedSize(true);

        //se crea el manejador del layout
        mLayoutManager = new LinearLayoutManager(this);

        //se le agrega el manejador a la vista
        myRecycleView.setLayoutManager(mLayoutManager);

        //se crea un nuevo adaptador con la lista de las recetas
        mAdapter = new MyAdapter(recetas,usuarios);

        //se le asigna el adaptador a la vista
        myRecycleView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new RecetasTask(recetas,myRecycleView,this,usuarios,0).execute();
    }


    public void publicarReceta(View view){
        Intent intent = new Intent(this,PosteoRecetaActivity.class);
        startActivity(intent);
    }

    public void editarPerfil(View view){
        Intent intent = new Intent(this, InicioSesionActivity.class);
        startActivity(intent);
    }


    @Override
    public void hecho() {
        int likesTotales = 0;
        for(int i=0;i<recetas.size();i++){
            likesTotales=likesTotales+recetas.get(i).getLikes();
        }
        textViewlikesTotales.setText(String.valueOf(likesTotales));
        numeroRecetas.setText(String.valueOf(recetas.size()));
    }
}
