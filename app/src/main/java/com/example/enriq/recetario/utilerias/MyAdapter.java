package com.example.enriq.recetario.utilerias;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.actividades.PosteoRecetaActivity;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.EliminacionRecetaTask;

import java.util.List;

/**
 * Created by enriq on 02/05/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {
    private List<Receta> recetas;
    private View.OnClickListener listener;
    private List<Usuario> usuarios;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView nombreReceta;
        TextView descripcion;
        Button botonEditar;
        Button botonEliminar;
        TextView textViewPublicador;
        TextView textViewLikes;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            nombreReceta = itemView.findViewById(R.id.textViewNombreReceta);
            descripcion = itemView.findViewById(R.id.textViewDescripcion);
            botonEditar = itemView.findViewById(R.id.buttonEditar);
            botonEliminar = itemView.findViewById(R.id.buttonEliminar);
            textViewPublicador = itemView.findViewById(R.id.textViewPublicador);
            textViewLikes = itemView.findViewById(R.id.textViewLikes);
        }
    }

    public MyAdapter(List<Receta> recetas,List<Usuario> usuarios) {
        this.recetas = recetas;
        this.usuarios = usuarios;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view2,null,false);
        ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(this);
        return vh;
    }



    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
       holder.nombreReceta.setText(recetas.get(position).getNombreReceta());
       holder.descripcion.setText(recetas.get(position).getDescripcion());
       holder.textViewPublicador.setText(usuarios.get(position).getNombre());
       holder.textViewLikes.setText(Integer.toString(recetas.get(position).getLikes()));
       holder.botonEditar.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               Intent intento = new Intent(view.getContext(),PosteoRecetaActivity.class);
               intento.putExtra("Receta",recetas.get(position));
               view.getContext().startActivity(intento);
           }
       });
       holder.botonEliminar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View view) {
               AlertDialog eliminar = new AlertDialog.Builder(view.getContext())
                       .setTitle("Eliminar receta")
                       .setMessage("¿Estas seguro que quieres borrar esta receta?")
                       .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               borrarReceta(view,recetas.get(position),usuarios.get(position));
                           }
                       })
                       .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.dismiss();
                           }
                       })
                       .create();
               eliminar.show();
           }
       });
    }

    private void borrarReceta(View view,Receta receta,Usuario usuario) {
        Toast.makeText(view.getContext(),"Borrado", Toast.LENGTH_SHORT).show();
        new EliminacionRecetaTask(receta,recetas, this,usuario,usuarios).execute();
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

}