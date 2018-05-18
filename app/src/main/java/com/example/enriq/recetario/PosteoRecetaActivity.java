package com.example.enriq.recetario;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
        receta = new Receta();
        receta.setIdReceta(receta.getIdReceta());
        receta.setNombreReceta(nombre.getText().toString());
        receta.setDescripcion(descripcion.getText().toString());
        receta.setCategoria(categoria.getSelectedItem().toString());
        receta.setIngredientes(ingredientes.getText().toString());
        receta.setPasos(pasos.getText().toString());
        receta.setLinkVideo(url.getText().toString());
        receta.setCorreo("enrique@gmail.com");
        //nuevaReceta.setImagenReceta(imagen.getI);
        new PosteoTask().execute();
    }

    class PosteoTask extends AsyncTask<Void,Void,Boolean>{
        boolean resultado=false;
        @Override
        protected Boolean doInBackground(Void... voids) {
            String strUrl = "http://192.168.0.105:11866/ForeignCook2/webresources/persistencia.recetas/"+receta.getCorreo();
            System.out.println(strUrl);
            String metodoEnvio = "POST";
            if(receta.getIdReceta()>0){
                strUrl+="/"+receta.getIdReceta();
                metodoEnvio="PUT";
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
                jsonObject.accumulate("ingredientes",receta.getIngredientes());
                jsonObject.accumulate("procedimiento",receta.getPasos());
                jsonObject.accumulate("categoria",receta.getCategoria());
                jsonObject.accumulate("likes",receta.getLikes());
                jsonObject.accumulate("linkVideo",receta.getLinkVideo());
                jsonObject.accumulate("correo",receta.getCorreo());

                /*
                String cadJson = "{";
                cadJson += "\"idReceta\":\""+receta.getIdReceta()+"\", ";
                cadJson += "\"nombreReceta\":\""+receta.getNombreReceta()+"\", ";
                cadJson += "\"ingredientes\":\""+receta.getIngredientes()+"\", ";
                cadJson += "\"procedimiento\": \""+receta.getPasos()+"\", ";
                cadJson += "\"categoria\": \""+receta.getCategoria()+"\", ";
                cadJson += "\"likes\": \""+receta.getLikes()+"\", ";
                cadJson += "\"linkVideo\": \""+receta.getLinkVideo()+"\", ";
                cadJson += "\"correo\": \""+receta.getCorreo()+"\" ";
                cadJson += "}";
                */
                System.out.println(jsonObject);
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(outputStream));
                escritor.write(String.valueOf(jsonObject));
                escritor.flush();

                InputStream input;
                if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    input = conn.getInputStream();
                } else {
                    input = conn.getErrorStream();

                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
                String cad = bufferedReader.readLine();
                System.out.println("cad" + cad);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(PosteoRecetaActivity.this);
                builder.setTitle("Guardado exitoso");
                builder.setMessage("La receta ha sido creada con éxito.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                PosteoRecetaActivity.this.finish();
                            }
                        });

                builder.create().show();
            } else {

            }
        }
    }


}
