package com.example.enriq.recetario.actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.modelo.Comentario;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.CargarComentariosTask;
import com.example.enriq.recetario.tareas.EnviarComentarioTask;
import com.example.enriq.recetario.utilerias.ComentariosAdapter;
import com.example.enriq.recetario.utilerias.TaskCallBack;

import java.util.ArrayList;
import java.util.List;

public class VerComentariosActivity extends AppCompatActivity implements TaskCallBack{
    private int idReceta;
    private List<Comentario> comentarios;
    private EditText editTextComentario;
    private ListView listView;
    private ComentariosAdapter adaptador;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_comentarios);
        idReceta = Integer.parseInt(getIntent().getStringExtra("Receta"));
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        editTextComentario = findViewById(R.id.editTextComentario);
        listView = findViewById(R.id.listView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        comentarios = new ArrayList<>();
        cargarComentarios();
    }

    public void cargarComentarios(){
        comentarios.clear();
        new CargarComentariosTask(idReceta,comentarios, this).execute();
    }

    public void Enviar(View view){
        Comentario comentario = new Comentario();
        comentario.setIdReceta(idReceta);
        comentario.setContenido(editTextComentario.getText().toString());
        comentario.setNombreUsuario(usuario.getNombre());
        new EnviarComentarioTask(idReceta,comentarios,comentario,this).execute();
        editTextComentario.setText("");
    }


    @Override
    public void hecho() {
        adaptador = new ComentariosAdapter(this,0,comentarios);
        listView.setAdapter(adaptador);
    }
}
