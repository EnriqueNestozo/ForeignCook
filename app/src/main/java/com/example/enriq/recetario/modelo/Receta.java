package com.example.enriq.recetario.modelo;

import android.graphics.Bitmap;

import com.example.enriq.recetario.utilerias.ProxyBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by enriq on 02/05/2018.
 */

public class Receta implements Serializable{
    private Bitmap imagenReceta=null;
    private ProxyBitmap bitmap;
    private String nombreReceta="";
    private String descripcion="";
    private String ingredientes="";
    private String pasos="";
    private String categoria="";
    private String linkVideo="";
    private int idReceta = 0;
    private int likes=0;
    private String correo="";

    public Receta(JSONObject jsonReceta) throws JSONException {
        this.idReceta = jsonReceta.getInt("idReceta");
        this.descripcion = jsonReceta.getString("descripcion");
        this.nombreReceta = jsonReceta.getString("nombreReceta");
        this.ingredientes = jsonReceta.getString("ingredientes");
        this.pasos = jsonReceta.getString("procedimiento");
        this.categoria = jsonReceta.getString("categoria");
        this.linkVideo = jsonReceta.getString("linkVideo");
        this.likes = jsonReceta.getInt("likes");
        this.correo = jsonReceta.getString("correo");
    }


    public ProxyBitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(ProxyBitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }



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

    @Override
    public String toString(){
        String recetaJSON = "{";
        recetaJSON += "\"idReceta\":\""+this.idReceta+"\",";
        recetaJSON += "\"nombreImagen\":\""+this.nombreReceta+"\"";
        recetaJSON += "}";
        return recetaJSON;
    }
}
