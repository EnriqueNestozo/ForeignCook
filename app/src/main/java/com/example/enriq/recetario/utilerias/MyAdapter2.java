package com.example.enriq.recetario.utilerias;


import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.enriq.recetario.R;
import com.example.enriq.recetario.actividades.VerComentariosActivity;
import com.example.enriq.recetario.actividades.VisualizarRecetaActivity;
import com.example.enriq.recetario.modelo.Receta;
import com.example.enriq.recetario.modelo.Usuario;
import com.example.enriq.recetario.tareas.PosteoTask;

import java.util.List;

/**
 * Created by enriq on 08/06/2018.
 */

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> implements View.OnClickListener{
    private List<Receta> recetas;
    private View.OnClickListener listener;
    private List<Usuario> usuarios;
    private Usuario usuario;

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView nombreReceta;
        private TextView descripcion;
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
            textViewPublicador = itemView.findViewById(R.id.textViewPublicador);
            textViewLikes = itemView.findViewById(R.id.textViewLikes);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentsButton = itemView.findViewById(R.id.commetsButton);
            foto = itemView.findViewById(R.id.imageView);
        }
    }

    public MyAdapter2(List<Receta> recetas, List<Usuario> usuarios, Usuario usuario) {
        this.recetas = recetas;
        this.usuarios = usuarios;
        this.usuario = usuario;
    }

    @Override
    public MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view1,null,false);
        MyAdapter2.ViewHolder vh = new MyAdapter2.ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(final MyAdapter2.ViewHolder holder, final int position) {
        System.out.println(position);
        holder.nombreReceta.setText(recetas.get(position).getNombreReceta());
        holder.descripcion.setText(recetas.get(position).getDescripcion());
        holder.textViewPublicador.setText(usuarios.get(position).getNombre());
        holder.textViewLikes.setText(Integer.toString(recetas.get(position).getLikes()));
        holder.foto.setImageBitmap(recetas.get(position).getBitmap().getBitmap());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(v.getContext(), VisualizarRecetaActivity.class);
                intento.putExtra("Receta",recetas.get(holder.getAdapterPosition()));
                v.getContext().startActivity(intento);
            }
        });
        holder.commentsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(view.getContext(), VerComentariosActivity.class);
                intento.putExtra("Receta",String.valueOf(recetas.get(position).getIdReceta()));
                intento.putExtra("usuario",usuario);
                view.getContext().startActivity(intento);
            }
        });
        holder.likeButton.setOnClickListener(new View.OnClickListener(){

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
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

