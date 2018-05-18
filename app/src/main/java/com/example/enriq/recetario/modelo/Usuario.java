package com.example.enriq.recetario.modelo;

import org.json.JSONException;
import org.json.JSONObject;

public class Usuario {
    private String nombre;
    private String descripcion;
    private String correo;
    private String nombreImagen;
    private String contraseña;

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Usuario(String nombre, String descripcion, String correo, int idUsuario, String nombreImagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.correo = correo;
        this.nombreImagen = nombreImagen;
    }

    public Usuario(){
        this.correo = "";
        this.contraseña = "";
        this.nombreImagen = "";
        this.nombre = "";
        this.descripcion = "";
    }

    public Usuario(JSONObject usuarioJSON){
        try {
            this.nombre = usuarioJSON.getString("nombre");
            this.descripcion = usuarioJSON.getString("descripcion");
            this.correo = usuarioJSON.getString("correo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    @Override
    public String toString(){
        String usuarioJSON = "{";
        usuarioJSON += "\"nombre\":\""+this.nombre+"\",";
        usuarioJSON += "\"correo\":\""+this.correo+"\",";
        usuarioJSON += "\"descripcion\":\""+this.descripcion+"\",";
        usuarioJSON += "\"contrasena\":\""+this.contraseña+"\",";
        usuarioJSON += "\"nombreImagen\":\""+this.nombreImagen+"\"";
        usuarioJSON += "}";
        
        return usuarioJSON;
    }
}