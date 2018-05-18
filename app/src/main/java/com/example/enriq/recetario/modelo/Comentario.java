package com.example.enriq.recetario.modelo;

import org.json.JSONException;
import org.json.JSONObject;

public class Comentario {
    private String contenido;
    private int idReceta;
    private String nombreUsuario;
    private int idComentario;

    public Comentario(String contenido, int idReceta, String nombreUsuario, int idComentario) {
        this.idComentario = idComentario;
        this.contenido = contenido;
        this.idReceta = idReceta;
        this.nombreUsuario = nombreUsuario;
    }

    public Comentario(){

    }

    public Comentario(JSONObject comentarioJSON){
        try {
            this.idComentario = comentarioJSON.getInt("idComentario");
            this.contenido = comentarioJSON.getString("contenido");
            this.idReceta = comentarioJSON.getInt("idReceta");
            this.nombreUsuario = comentarioJSON.getString("nombreUsuario");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }
}
