package com.example.julian.agromobile.util;

import com.example.julian.agromobile.models.Aeronave;
import com.example.julian.agromobile.models.Camara;

import java.text.DecimalFormat;

/**
 * Created by JULIAN on 14/04/2017.
 */

public class Vuelo {
      public double calcularAnchoEntreLineas(Camara camara, Aeronave aeronave,int altura){
          float mult1=camara.getLongitudHorizontalSensor();
          float mult2 = altura/camara.getLongitudFocal();
          double mult3=0.3;

          double resultado=mult1*mult2*mult3;
          return resultado;
      }
    public double calcularAnchoEntreCapturas(Camara camara, Aeronave aeronave,int altura){
        float mult1=camara.getLongitudVerticalSensor();
        float mult2 = altura/camara.getLongitudFocal();
        double mult3=0.3;

        double resultado=mult1*mult2*mult3;

        return resultado;
    }
    public double calcularVelocidad(double anchocaptura,Camara camara){

        return anchocaptura*camara.getVelocidadCaptura();
    }
    public double calcularResolucion(Camara camara, int altura){
        double longPixel=camara.getLongitudHorizontalSensor()/camara.getResolucionHorizontal();

        return (longPixel*altura)/camara.getLongitudFocal();
    }
}
