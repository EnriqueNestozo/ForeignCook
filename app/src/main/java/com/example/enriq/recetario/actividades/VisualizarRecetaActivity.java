package com.example.enriq.recetario.actividades;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.utilerias.Constantes;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class VisualizarRecetaActivity extends AppCompatActivity {
    private Receta receta;
    private TextView nombreReceta;
    private TextView descripcion;
    private TextView ingredientes;
    private TextView procedimiento;
    private TextView categoria;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_receta);
        nombreReceta = findViewById(R.id.textViewNombreReceta);
        descripcion = findViewById(R.id.textViewDescripcion);
        ingredientes = findViewById(R.id.textViewIngredientes);
        procedimiento = findViewById(R.id.textViewProcedimiento);
        categoria = findViewById(R.id.textViewCategoria);
        imagen = findViewById(R.id.imageView);

        receta = (Receta) getIntent().getSerializableExtra("Receta");
        nombreReceta.setText(receta.getNombreReceta());
        descripcion.setText(receta.getDescripcion());
        ingredientes.setText(receta.getIngredientes());
        procedimiento.setText(receta.getPasos());
        categoria.setText(receta.getCategoria());
        imagen.setImageBitmap(receta.getBitmap().getBitmap());
        if(receta.getLinkVideo()!=null){
            validarURL(receta.getLinkVideo());
        }else{
            Toast.makeText(this, "Esta receta no tiene video", Toast.LENGTH_SHORT).show();
        }



    }

    private void validarURL(String url){
        if(!url.trim().equals("")){
            if(url.contains("youtube") || url.contains("youtu.be")){
                System.out.println("Pagina valida");
                String[] separacion = url.split("/|//");
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
}
