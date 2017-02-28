package com.example.julian.agromobile.models;

/**
 * Created by JULIAN on 18/02/2017.
 */

public class Camara {

    private String id;
    private String refCamara;
    private float longitudFocal;
    private int velocidadCaptura; //fotos/segundo
    private int resolucionHorizontal;
    private int resolucionVertical;
    private float longitudHorizontalSensor;
    private float longitudVerticalSensor;

    public String getId() {
        return id;
    }

    public int getVelocidadCaptura() {
        return velocidadCaptura;
    }

    public void setVelocidadCaptura(int velocidadCaptura) {
        this.velocidadCaptura = velocidadCaptura;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefCamara() {
        return refCamara;
    }

    public void setRefCamara(String refCamara) {
        this.refCamara = refCamara;
    }

    public float getLongitudFocal() {
        return longitudFocal;
    }

    public void setLongitudFocal(float longitudFocal) {
        this.longitudFocal = longitudFocal;
    }

    public int getResolucionHorizontal() {
        return resolucionHorizontal;
    }

    public void setResolucionHorizontal(int resolucionHorizontal) {
        this.resolucionHorizontal = resolucionHorizontal;
    }

    public int getResolucionVertical() {
        return resolucionVertical;
    }

    public void setResolucionVertical(int resolucionVertical) {
        this.resolucionVertical = resolucionVertical;
    }

    public float getLongitudHorizontalSensor() {
        return longitudHorizontalSensor;
    }

    public void setLongitudHorizontalSensor(float longitudHorizontalSensor) {
        this.longitudHorizontalSensor = longitudHorizontalSensor;
    }

    public float getLongitudVerticalSensor() {
        return longitudVerticalSensor;
    }

    public void setLongitudVerticalSensor(float longitudVerticalSensor) {
        this.longitudVerticalSensor = longitudVerticalSensor;
    }
}
