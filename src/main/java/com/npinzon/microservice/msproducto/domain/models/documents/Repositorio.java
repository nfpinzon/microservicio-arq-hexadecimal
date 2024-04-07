package com.npinzon.microservice.msproducto.domain.models.documents;

public class Repositorio {

    private String Nombre;
    private String conexion;

    
    public Repositorio(String nombre, String conexion) {
        Nombre = nombre;
        this.conexion = conexion;
    }

    public Repositorio() {
    }
    
    public String getNombre() {
        return Nombre;
    }
    public void setNombre(String nombre) {
        Nombre = nombre;
    }
    public String getConexion() {
        return conexion;
    }
    public void setConexion(String conexion) {
        this.conexion = conexion;
    }
    
}
