package com.example.enriq.recetario.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.RecetasTask;
import com.example.enriq.recetario.utilerias.MyAdapter2;
import com.example.enriq.recetario.utilerias.TaskCallBack;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalActivity extends AppCompatActivity implements TaskCallBack{

    private RecyclerView myRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<Receta> recetas = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
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
            Intent intento = new Intent(this, MisRecetasActivity.class);
            startActivity(intento);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        myRecycleView = findViewById(R.id.my_recycler_view);
        myRecycleView.setHasFixedSize(true);
        //se crea el manejador del layout
        mLayoutManager = new LinearLayoutManager(this);

        //se le agrega el manejador a la vista
        myRecycleView.setLayoutManager(mLayoutManager);

        //se crea un nuevo adaptador con la lista de las recetas
        mAdapter = new MyAdapter2(recetas,usuarios);

        //se le asigna el adaptador a la vista
        myRecycleView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new RecetasTask(recetas,myRecycleView,this,usuarios,1).execute();
    }


    public void publicarReceta(View view){
        Intent intent = new Intent(this,PosteoRecetaActivity.class);
        startActivity(intent);
    }

    @Override
    public void hecho() {

    }
}
