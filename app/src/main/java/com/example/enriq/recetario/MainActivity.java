package com.example.enriq.recetario;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.actividades.InicioSesionActivity;
import com.example.enriq.recetario.actividades.PosteoRecetaActivity;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.RecetasTask;
import com.example.enriq.recetario.modelo.Receta;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView myRecycleView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    FloatingActionButton floatingActionButton;
    List<Receta> recetas = new ArrayList<>();
    List<Usuario> usuarios = new ArrayList<>();
    TextView textViewlikesTotales;
    TextView textViewNumeroRecetas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        myRecycleView = findViewById(R.id.my_recycler_view);
        textViewlikesTotales = findViewById(R.id.textViewLikes);
        textViewNumeroRecetas = findViewById(R.id.textViewNombreReceta);
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

    public void setUsuarios(List<Usuario> usuarios){
        this.usuarios = usuarios;
    }

    public void setLikes(int likesTotales){
        textViewlikesTotales.setText(String.valueOf(likesTotales));
        textViewNumeroRecetas.setText(recetas.size());
    }


}
