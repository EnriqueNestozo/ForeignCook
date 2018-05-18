package com.example.enriq.recetario.actividades;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.InicioSesionTask;
import com.example.enriq.recetario.utilerias.Validacion;

public class InicioSesionActivity extends AppCompatActivity {

    private TextView tfContraseña;
    private TextView tfCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        tfContraseña = findViewById(R.id.tfContraseña);
        tfCorreo = findViewById(R.id.tfCorreo);
    }

    public void accionIniciarSesion(View vista){
        if(validarCampos()){
            System.out.println("Hay campos vacios");
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
}
