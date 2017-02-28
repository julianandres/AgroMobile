package com.example.julian.agromobile.models;

/**
 * Created by JULIAN on 18/02/2017.
 */

public class Aeronave {
    private String id;
    private String referencia;
    private int autonomia;//entero en minutos
    private int velocidadCrucero;
    private int altura;
    private String idUsuario;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getAutonomia() {
        return autonomia;
    }

    public void setAutonomia(int autonomia) {
        this.autonomia = autonomia;
    }

    public int getVelocidadCrucero() {
        return velocidadCrucero;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setVelocidadCrucero(int velocidadCrucero) {
        this.velocidadCrucero = velocidadCrucero;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
}
