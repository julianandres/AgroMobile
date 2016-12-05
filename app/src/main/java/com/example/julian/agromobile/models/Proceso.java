package com.example.julian.agromobile.models;

/**
 * Created by JULIAN on 30/11/2016.
 */

public class Proceso {

    private String id;
    private String nombre;
    private String fechaInicio;
    private String tipoCultivo;
    private String duracionSemanas;
    private int numeroSubprocesos;
    private boolean state;
    private String fechaFin;
    private int subProcesoActual;

    public String getId() {
        return id;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getTipoCultivo() {
        return tipoCultivo;
    }

    public void setTipoCultivo(String tipoCultivo) {
        this.tipoCultivo = tipoCultivo;
    }

    public String getDuracionSemanas() {
        return duracionSemanas;
    }

    public void setDuracionSemanas(String duracionSemanas) {
        this.duracionSemanas = duracionSemanas;
    }

    public int getNumeroSubprocesos() {
        return numeroSubprocesos;
    }

    public void setNumeroSubprocesos(int numeroSubprocesos) {
        this.numeroSubprocesos = numeroSubprocesos;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getSubProcesoActual() {
        return subProcesoActual;
    }

    public void setSubProcesoActual(int subProcesoActual) {
        this.subProcesoActual = subProcesoActual;
    }


}
