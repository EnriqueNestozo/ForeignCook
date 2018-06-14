package com.example.enriq.recetario.actividades;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.EditarUsuarioTask;
import com.example.enriq.recetario.tareas.RegistroUsuarioTask;
import com.example.enriq.recetario.utilerias.Formato;
import com.example.enriq.recetario.utilerias.Validacion;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;



public class RegistrarUsuarioActivity extends AppCompatActivity {

    private EditText tfCorreo;
    private EditText tfContraseña;
    private EditText tfReContraseña;
    private EditText tfDescipcion;
    private TextView labelContrasena;
    private TextView labelReContrasena;
    private CircleImageView imagen;
    private EditText tfNombreUsuario;
    private Bitmap archivo;
    private boolean edicion;
    private Usuario usuario;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        usuario = (Usuario)getIntent().getSerializableExtra("usuario");
        labelContrasena = findViewById(R.id.labelContrasena);
        labelReContrasena = findViewById(R.id.labelReContrasena);
        tfContraseña = findViewById(R.id.tfContraseña);
        tfCorreo = findViewById(R.id.tfCorreo);
        tfReContraseña = findViewById(R.id.tfRecontraseña);
        tfDescipcion = findViewById(R.id.tfDescripcion);
        tfNombreUsuario = findViewById(R.id.tfNombreUsuario);
        imagen = findViewById(R.id.imagen);
        if(usuario != null){
            imagen.setImageBitmap(MenuPrincipalActivity.usuario.getBitsImagen());
            tfContraseña.setVisibility(View.INVISIBLE);
            tfReContraseña.setVisibility(View.INVISIBLE);
            labelReContrasena.setVisibility(View.INVISIBLE);
            labelContrasena.setVisibility(View.INVISIBLE);
            tfCorreo.setText(MenuPrincipalActivity.usuario.getCorreo());
            tfNombreUsuario.setText(MenuPrincipalActivity.usuario.getNombre());
            tfDescipcion.setText(MenuPrincipalActivity.usuario.getDescripcion());
            edicion = true;
            imagen.setImageBitmap(usuario.getProxyBitmap().getBitmap());
        }
    }

    public void accionRegistrarUsuario(View vista){
        if(edicion){
            if(validarCamposEdicion()){
                Toast.makeText(this, "Hay campos vacios", Toast.LENGTH_LONG).show();
            }else {
                Usuario usuario = new Usuario();
                usuario.setNombre(tfNombreUsuario.getText().toString().trim());
                usuario.setContraseña(MenuPrincipalActivity.usuario.getContraseña());
                if (!tfDescipcion.getText().toString().trim().isEmpty()) {
                    usuario.setDescripcion(tfDescipcion.getText().toString().trim());
                }
                usuario.setCorreo(tfCorreo.getText().toString().trim());
                usuario.setNombreImagen(MenuPrincipalActivity.usuario.getNombreImagen());
                if(archivo != null){
                    MenuPrincipalActivity.usuario.setBitsImagen(archivo);
                }
                new EditarUsuarioTask(usuario,this,archivo).execute();
            }
        }else {
            if (validarCampos()) {
                mostrarMensajeError("Hay campos vacios!");
            } else {
                if (validarContraseña()) {
                    if (Validacion.validarFormatoCorreo(tfCorreo.getText().toString().trim())) {

                        Usuario usuario = new Usuario();
                        usuario.setContraseña(Formato.encriptarContrasena(tfContraseña.getText().toString().trim()));
                        usuario.setNombre(tfNombreUsuario.getText().toString().trim());
                        usuario.setNombreImagen(tfCorreo.getText().toString().trim());
                        if (!tfDescipcion.getText().toString().trim().isEmpty()) {
                            usuario.setDescripcion(tfDescipcion.getText().toString().trim());
                        }
                        usuario.setCorreo(tfCorreo.getText().toString().trim());
                        new RegistroUsuarioTask(usuario, this, archivo).execute();

                    } else {
                        Toast.makeText(this, "El formato del correo no es correcto", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mostrarMensajeError("La contraseña debe de tener 8 caracteres, una mayuscula y un número o no coincide las contraseñas");
                }
            }
        }
    }

    private boolean validarCamposEdicion() {
        return tfCorreo.getText().toString().trim().isEmpty() || tfNombreUsuario.getText().toString().trim().isEmpty();
    }

    private boolean validarContraseña(){
        return  tfContraseña.getText().toString().trim().equals(tfReContraseña.getText().toString().trim()) && Validacion.validarFormatoContraseña(tfContraseña.getText().toString().trim());
    }

    private boolean validarCampos(){
        return tfCorreo.getText().toString().trim().isEmpty() || tfContraseña.getText().toString().trim().isEmpty() || tfNombreUsuario.getText().toString().trim().isEmpty()
                || tfReContraseña.getText().toString().trim().isEmpty();
    }

    public void cargarImagen(View vista){
        Intent intento = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intento.setType("image/");
        startActivityForResult(intento.createChooser(intento,"Seleccione"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            Uri ruta = data.getData();
            imagen.setImageURI(ruta);
            try {
                archivo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),ruta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void mostrarMensajeError(String mensaje){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(RegistrarUsuarioActivity.this);
        dialogo.setTitle("Ops!");
        dialogo.setMessage(mensaje).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogo.show();
    }
}
