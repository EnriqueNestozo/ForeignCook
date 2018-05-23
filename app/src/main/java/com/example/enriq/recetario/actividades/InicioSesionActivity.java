package com.example.enriq.recetario.actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.InicioSesionTask;
import com.example.enriq.recetario.utilerias.Validacion;
import de.hdodenhof.circleimageview.CircleImageView;

public class InicioSesionActivity extends AppCompatActivity {

    private EditText tfContraseña;
    private EditText tfCorreo;
    private CircleImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        tfContraseña = findViewById(R.id.tfContraseña);
        imagen = findViewById(R.id.imagen);
        tfCorreo = findViewById(R.id.tfCorreo);
    }

    public void accionIniciarSesion(View vista){
        if(validarCampos()){
            AlertDialog.Builder dialogo = new AlertDialog.Builder(InicioSesionActivity.this);
            dialogo.setTitle("Ops!");
            dialogo.setMessage("Hay campos vacios").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    
                }
            });
            dialogo.show();
        }else{
            if(Validacion.validarFormatoCorreo(tfCorreo.getText().toString())){
                if(Validacion.validarFormatoContraseña(tfContraseña.getText().toString())){
                    new InicioSesionTask(tfCorreo.getText().toString().trim(),tfContraseña.getText().toString().trim(),this).execute();
                }else{

                }
            }
        }
    }

    private boolean validarCampos(){
        return tfContraseña.getText().toString().trim().isEmpty() || tfCorreo.getText().toString().trim().isEmpty();
    }

    public void cargarInicioSesion(Usuario usuario){
        if(usuario != null){
            AlertDialog.Builder dialogo = new AlertDialog.Builder(InicioSesionActivity.this);
            dialogo.setTitle("Inicio de sesion");
            dialogo.setMessage("BIENVENIDO").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    InicioSesionActivity.this.finish();
                }
            });
            dialogo.show();
        }
    }

    public void accionRegistrarUsuario(View vista){
        Intent intento = new Intent(this, RegistrarUsuarioActivity.class);
        startActivity(intento);
    }
}
