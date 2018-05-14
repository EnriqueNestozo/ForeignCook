package com.example.enriq.recetario;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by enriq on 02/05/2018.
 */

public class Receta implements Serializable{
    private Bitmap imagenReceta=null;
    private String nombreReceta="";
    private String descripcion="";
    private String ingredientes="";
    private String pasos="";
    private String categoria="";
    private String linkVideo="";

    public Bitmap getImagenReceta() {
        return imagenReceta;
    }

    public void setImagenReceta(Bitmap imagenReceta) {
        this.imagenReceta = imagenReceta;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getPasos() {
        return pasos;
    }

    public void setPasos(String pasos) {
        this.pasos = pasos;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public Receta(){
    }

    public Receta(String nombreReceta,String descripcion){
        this.nombreReceta = nombreReceta;
        this.descripcion = descripcion;
    }
}
