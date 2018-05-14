package com.example.enriq.recetario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class PosteoRecetaActivity extends AppCompatActivity {
    Receta receta;
    EditText nombre;
    Spinner categoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posteo_receta);

        List<String> categorias = new ArrayList<>();
        categorias.add("Desayuno");
        categorias.add("Comida");
        categorias.add("Cena");
        categorias.add("Postre");
        categorias.add("Almuerzo");
        categorias.add("Merienda");
        categorias.add("Snack");
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this,android.R.layout.simple_list_item_activated_1,categorias);
        adaptador.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);

        categoria = findViewById(R.id.spinnerCategoria);
        categoria.setAdapter(adaptador);
        receta = (Receta) getIntent().getSerializableExtra("Receta");
        if(receta!=null){
            nombre = findViewById(R.id.editTextNombre);
            nombre.setText(receta.getNombreReceta());
        }



    }


}
