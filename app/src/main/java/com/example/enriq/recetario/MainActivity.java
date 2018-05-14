package com.example.enriq.recetario;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView myRecycleView;
    private FloatingActionButton floatingActionButton;
    RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<Receta> recetas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Receta receta1 = new Receta("Papas con fideo","Esta receta consiste en una rica pasta con papas");
        Receta receta2 = new Receta("Pollo a la crema","Esta receta consiste en pechugas de pollo bañadas con crema y especias");
        Receta receta3 = new Receta("Papas con Salchicha","Esta receta es ideal para aperitivo");
        Receta receta4 = new Receta("Papas con fideo","Esta receta consiste en una rica pasta con papas");
        Receta receta5 = new Receta("Pollo a la crema","Esta receta consiste en pechugas de pollo bañadas con crema y especias");
        Receta receta6 = new Receta("Papas con Salchicha","Esta receta es ideal para aperitivo");
        Receta receta7 = new Receta("Papas con fideo","Esta receta consiste en una rica pasta con papas");
        Receta receta8 = new Receta("Pollo a la crema","Esta receta consiste en pechugas de pollo bañadas con crema y especias, ideal para cuando no sabes que preparar, facil, rápido y divertido");
        Receta receta9 = new Receta("Papas con Salchicha","Esta receta es ideal para aperitivo");
        recetas.add(receta1);
        recetas.add(receta2);
        recetas.add(receta3);
        recetas.add(receta4);
        recetas.add(receta5);
        recetas.add(receta6);
        recetas.add(receta7);
        recetas.add(receta8);
        recetas.add(receta9);

        myRecycleView = findViewById(R.id.my_recycler_view);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        myRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        myRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(recetas);

        myRecycleView.setAdapter(mAdapter);


    }

    public void publicarReceta(View view){
        Intent intent = new Intent(this,PosteoRecetaActivity.class);
        startActivity(intent);
    }
}
