package com.example.julian.agromobile.models;

import java.util.Date;

/**
 * Created by JULIAN on 30/11/2016.
 */

public class Proceso {

    private String id;
    private String nombre;
    private String idUsuario;
    private String idAeronave;
    private String idCamara;
    private Date fechaInicio;
    private int duracionSemanas;
    private int numeroSubprocesos;
    private boolean state;
    private Date fechaFin;
    private int subProcesoActual;
    private double distLineasVuelo;
    private double distCapturas;
    private double velocidadCaptura;
    private int alturaVuelo;
    private double resolucionVuelo;

    public String getIdAeronave() {
        return idAeronave;
    }

    public void setIdAeronave(String idAeronave) {
        this.idAeronave = idAeronave;
    }

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

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdCamara() {
        return idCamara;
    }

    public void setIdCamara(String idCamara) {
        this.idCamara = idCamara;
    }

    public double getDistLineasVuelo() {
        return distLineasVuelo;
    }

    public void setDistLineasVuelo(double distLineasVuelo) {
        this.distLineasVuelo = distLineasVuelo;
    }

    public double getDistCapturas() {
        return distCapturas;
    }

    public void setDistCapturas(double distCapturas) {
        this.distCapturas = distCapturas;
    }

    public double getVelocidadCaptura() {
        return velocidadCaptura;
    }

    public void setVelocidadCaptura(double velocidadCaptura) {
        this.velocidadCaptura = velocidadCaptura;
    }

    public Proceso(){

    }

    public double getResolucionVuelo() {
        return resolucionVuelo;
    }

    public void setResolucionVuelo(double resolucionVuelo) {
        this.resolucionVuelo = resolucionVuelo;
    }

    public int getAlturaVuelo() {
        return alturaVuelo;
    }

    public void setAlturaVuelo(int alturaVuelo) {
        this.alturaVuelo = alturaVuelo;
    }
}
