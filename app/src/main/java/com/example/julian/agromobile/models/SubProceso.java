package com.example.julian.agromobile.models;

import java.util.Date;

/**
 * Created by JULIAN on 07/12/2016.
 */

public class SubProceso {

    String id;
    Date fecha;
    String nombre;
    String idProceso;
    int numeroenProceso;
    int estado;
    int fotorgb;
    String urlResultado;
    int fotonoir;

    public int getFotorgb() {
        return fotorgb;
    }

    public void setFotorgb(int fotorgb) {
        this.fotorgb = fotorgb;
    }

    public int getFotonoir() {
        return fotonoir;
    }

    public void setFotonoir(int fotonoir) {
        this.fotonoir = fotonoir;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }

    public int getNumeroenProceso() {
        return numeroenProceso;
    }

    public void setNumeroenProceso(int numeroenProceso) {
        this.numeroenProceso = numeroenProceso;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getUrlResultado() {
        return urlResultado;
    }

    public void setUrlResultado(String urlResultado) {
        this.urlResultado = urlResultado;
    }
}
