package com.example.enriq.recetario;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.enriq.recetario.tareas.PosteoTask;
import com.example.enriq.recetario.utilerias.Constantes;

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
import java.util.ArrayList;
import java.util.List;

public class PosteoRecetaActivity extends AppCompatActivity {
    Receta receta;
    EditText nombre;
    Spinner categoria;
    EditText descripcion;
    EditText ingredientes;
    EditText pasos;
    EditText url;
    ImageView imagen;
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
        descripcion = findViewById(R.id.editTextDescripcion);
        ingredientes = findViewById(R.id.editTextIngredientes);
        pasos = findViewById(R.id.editTextPasos);
        url = findViewById(R.id.editTextVideo);
        imagen = findViewById(R.id.imageView);
        nombre = findViewById(R.id.editTextNombre);

        categoria.setAdapter(adaptador);
        receta = (Receta) getIntent().getSerializableExtra("Receta");
        if(receta!=null){
            nombre.setText(receta.getNombreReceta());
            ingredientes.setText(receta.getIngredientes());
            pasos.setText(receta.getPasos());
        }



    }



    public void publicar(View view){
        if(!validarCamposObligatorios()){
            receta = new Receta();
            Context context = this;
            receta.setIdReceta(receta.getIdReceta());
            receta.setNombreReceta(nombre.getText().toString());
            receta.setDescripcion(descripcion.getText().toString());
            receta.setCategoria(categoria.getSelectedItem().toString());
            receta.setIngredientes(ingredientes.getText().toString());
            receta.setPasos(pasos.getText().toString());
            receta.setLinkVideo(url.getText().toString());
            receta.setCorreo("enrique@gmail.com");
            //nuevaReceta.setImagenReceta(imagen.getI);
            new PosteoTask(receta,this).execute();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(PosteoRecetaActivity.this);
            builder.setTitle("Campos vacíos");
            builder.setMessage("Existen campos obligatorios que estan vacíos")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            builder.create().show();
        }
    }


    private boolean validarCamposObligatorios() {
        boolean validos = true;
        if(nombre.getText().equals("") || ingredientes.getText().equals("") || pasos.getText().equals("")){
            validos = false;
        }
        return validos;
    }
}
