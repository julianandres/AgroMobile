package com.example.julian.agromobile.models;

import java.util.Date;

/**
 * Created by JULIAN on 30/11/2016.
 */

public class Proceso {

    private String id;
    private String nombre;
    private Date fechaInicio;
    private int duracionSemanas;
    private int numeroSubprocesos;
    private boolean state;
    private Date fechaFin;
    private int subProcesoActual;

    public String getId() {
        return id;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getDuracionSemanas() {
        return duracionSemanas;
    }

    public void setDuracionSemanas(int duracionSemanas) {
        this.duracionSemanas = duracionSemanas;
    }

    public int getNumeroSubprocesos() {
        return numeroSubprocesos;
    }

    public void setNumeroSubprocesos(int numeroSubprocesos) {
        this.numeroSubprocesos = numeroSubprocesos;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getSubProcesoActual() {
        return subProcesoActual;
    }

    public void setSubProcesoActual(int subProcesoActual) {
        this.subProcesoActual = subProcesoActual;
    }
}
