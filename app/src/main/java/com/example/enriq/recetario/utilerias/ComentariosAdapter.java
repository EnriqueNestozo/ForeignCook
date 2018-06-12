package com.example.enriq.recetario.utilerias;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.modelo.Comentario;

import java.util.List;

/**
 * Created by enriq on 11/06/2018.
 */

public class ComentariosAdapter extends ArrayAdapter {
    private List<Comentario> comentarios;
    private Context context;


    public ComentariosAdapter(@NonNull Context context, int resource, @NonNull List<Comentario> comentarios){
        super(context,resource,comentarios);
        this.comentarios = comentarios;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        System.out.println("aqui");
        listItem = LayoutInflater.from(context).inflate(R.layout.comentarios,parent,false);
        if(position>=0 && position<=comentarios.size()){
            TextView com = listItem.findViewById(R.id.textViewComentario);
            TextView correo = listItem.findViewById(R.id.textViewUsuario);
            com.setText(comentarios.get(position).getContenido());
            correo.setText(comentarios.get(position).getNombreUsuario());
            System.out.println("usuario"+correo.getText());
        }
        return listItem;
    }
}
