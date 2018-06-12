package com.example.enriq.recetario.utilerias;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enriq.recetario.R;
import com.example.enriq.recetario.actividades.PosteoRecetaActivity;
import com.example.enriq.recetario.actividades.VerComentariosActivity;
import com.example.enriq.recetario.actividades.VisualizarRecetaActivity;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.EliminacionRecetaTask;
import com.example.enriq.recetario.tareas.PosteoTask;

import java.util.List;

/**
 * Created by enriq on 02/05/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {
    private List<Receta> recetas;
    private View.OnClickListener listener;
    private List<Usuario> usuarios;
    private Usuario usuario;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView nombreReceta;
        private TextView descripcion;
        private Button botonEditar;
        private Button botonEliminar;
        private TextView textViewPublicador;
        private TextView textViewLikes;
        private ImageView likeButton;
        private ImageView commentsButton;
        private ImageView foto;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            nombreReceta = itemView.findViewById(R.id.textViewNombreReceta);
            descripcion = itemView.findViewById(R.id.textViewDescripcion);
            botonEditar = itemView.findViewById(R.id.buttonEditar);
            botonEliminar = itemView.findViewById(R.id.buttonEliminar);
            textViewPublicador = itemView.findViewById(R.id.textViewPublicador);
            textViewLikes = itemView.findViewById(R.id.textViewLikes);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentsButton = itemView.findViewById(R.id.commetsButton);
            foto = itemView.findViewById(R.id.imageView);
        }
    }

    public MyAdapter(List<Receta> recetas,List<Usuario> usuarios,Usuario usuario) {
        this.recetas = recetas;
        this.usuarios = usuarios;
        this.usuario = usuario;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view,null,false);
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
       holder.foto.setImageBitmap(recetas.get(position).getBitmap().getBitmap());
       holder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intento = new Intent(view.getContext(), VisualizarRecetaActivity.class);
               intento.putExtra("Receta",recetas.get(holder.getAdapterPosition()));
               view.getContext().startActivity(intento);
           }
       });
       holder.likeButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(!holder.likeButton.getTag().toString().equals("like")) {
                   holder.likeButton.setImageResource(R.drawable.like);
                   recetas.get(position).setLikes(recetas.get(position).getLikes()+1);
                   holder.likeButton.setTag("like");
                   new PosteoTask(recetas.get(position), (TaskCallBack) view.getContext()).execute();
                   notifyDataSetChanged();
               }else {
                   holder.likeButton.setImageResource(R.drawable.dislike);
                   recetas.get(position).setLikes(recetas.get(position).getLikes()-1);
                   holder.likeButton.setTag("dislike");
                   new PosteoTask(recetas.get(position), (TaskCallBack) view.getContext()).execute();
                   notifyDataSetChanged();
               }
           }
       });

       holder.commentsButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intento = new Intent(view.getContext(), VerComentariosActivity.class);
               intento.putExtra("Receta",String.valueOf(recetas.get(position).getIdReceta()));
               intento.putExtra("usuario",usuario);
               view.getContext().startActivity(intento);
           }
       });

        holder.botonEditar.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               Intent intento = new Intent(view.getContext(),PosteoRecetaActivity.class);
               Bitmap resized = Bitmap.createScaledBitmap(recetas.get(position).getBitmap().getBitmap(), 200, 200, true);
               recetas.get(position).setBitmap(new ProxyBitmap(resized));
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
