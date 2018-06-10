package com.example.enriq.recetario.actividades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.tareas.PosteoTask;
import com.example.enriq.recetario.utilerias.Constantes;
import com.example.enriq.recetario.utilerias.TaskCallBack;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class PosteoRecetaActivity extends AppCompatActivity implements TaskCallBack{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_FILE = 0;
    private Receta receta;
    private EditText nombre;
    private Spinner categoria;
    private EditText descripcion;
    private EditText ingredientes;
    private EditText pasos;
    private EditText url;
    private ImageView imagen;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;
    private Toolbar myToolbar;
    private Bitmap foto;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posteo_receta);
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
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
        imagen = findViewById(R.id.imageViewReceta);
        nombre = findViewById(R.id.editTextNombre);


        url.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validarURL();
            }
        });

        categoria.setAdapter(adaptador);
        receta = (Receta) getIntent().getSerializableExtra("Receta");
        if(receta!=null){
            nombre.setText(receta.getNombreReceta());
            ingredientes.setText(receta.getIngredientes());
            pasos.setText(receta.getPasos());
            descripcion.setText(receta.getDescripcion());
            url.setText(receta.getLinkVideo());
        }else{
            receta = new Receta();
        }

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Tomar foto", "Elegir de la galería", "Cancelar"};
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Agrega una foto");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Tomar foto")) {
                            dispatchTakePictureIntent();
                        } else if (items[item].equals("Elegir de la galería")) {
                            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent,SELECT_FILE);
                        } else if (items[item].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });



    }
    //delega la actividad de tomar la foto
    private void dispatchTakePictureIntent() {
        Intent foto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //si hay alguna actividad externa que puede realizar el intento de tomar foto inicia la primera
        if (foto.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = crearImagen();
            } catch(Exception e ) {

            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.enriq.fileprovider", photoFile);
                foto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(foto, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private String fotoActual;

    private File crearImagen() throws IOException {
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName = "JPEG_"+fecha+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imgFileName, ".jpg", storageDir);
        fotoActual = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            foto = (Bitmap) extras.get("data");
            imagen.setImageBitmap(foto);
        }else if(requestCode==SELECT_FILE && resultCode == RESULT_OK){
            Uri ruta = data.getData();
            imagen.setImageURI(ruta);
            try {
                foto = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),ruta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void publicar(View view){
        if(validarCamposObligatorios()){
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

    private void validarURL(){
        if(!url.getText().toString().trim().equals("")){
            if(url.getText().toString().contains("youtube") || url.getText().toString().contains("youtu.be")){
                System.out.println("Pagina valida");
                String[] separacion = url.getText().toString().split("/|//");
                if(separacion.length>1){
                    if(separacion[separacion.length-1].contains("=")){
                        //Es una url sacada de escritorio
                        String[] separacion2 = separacion[separacion.length-1].split("=");
                        final String key = separacion2[separacion2.length-1];
                        System.out.println("url de escritorio");
                        System.out.println(key);
                        initializeYoutubePlayer(key);
                    }else{
                        if(separacion[separacion.length-1].contains(".")){
                            System.out.println("no es valida");
                        }else{
                            //Es una url copiada de celular
                            String key = separacion[separacion.length-1];
                            System.out.println("url de celular");
                            System.out.println(key);
                            initializeYoutubePlayer(key);
                        }

                    }
                }

            }else{
                System.out.println("Pagina invalida");
            }
        }
    }

    private void initializeYoutubePlayer(final String url) {
        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(Constantes.youtubeKey, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //cue the 1st video by default
                    youTubePlayer.cueVideo(url);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                System.out.println("Falló, " + arg1);
            }
        });
    }


    private boolean validarCamposObligatorios() {
        boolean validos = true;
        if(nombre.getText().toString().trim().equals("") || ingredientes.getText().toString().trim().equals("") || pasos.getText().toString().trim().equals("")){
            validos = false;
        }


        return validos;
    }

    @Override
    public void hecho() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Guardado exitoso");
        builder.setMessage("La receta ha sido creada con éxito.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });

        builder.create().show();

    }
}
