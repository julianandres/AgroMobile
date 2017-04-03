package com.example.julian.agromobile.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.julian.agromobile.R;
import com.example.julian.agromobile.models.Camara;

import java.util.List;

/**
 * Created by JULIAN on 26/02/2017.
 */

public class CameraAdapter extends BaseAdapter {
    Context context;
    List<Camara> data;
    int positionSelect;
    public CameraAdapter(Context context, List<Camara> data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if(convertView == null)
            v = View.inflate(context, R.layout.template_cameras, null);
        else
            v = convertView;

        Camara camara= (Camara) getItem(position);
        TextView  refCamara= (TextView) v.findViewById(R.id.template_camera_ref);
        TextView  longitudFocal= (TextView) v.findViewById(R.id.template_camera_focal);
        TextView  velocidadCaptura= (TextView) v.findViewById(R.id.template_camera_capturespeed); //fotos/segundo
        TextView  resolucion= (TextView) v.findViewById(R.id.template_camera_resolution);
        TextView  longitudSensor= (TextView) v.findViewById(R.id.template_camera_sensorsize);
        refCamara.setText(camara.getRefCamara());
        longitudFocal.setText(camara.getLongitudFocal()+"");
        velocidadCaptura.setText(camara.getVelocidadCaptura()+context.getString(R.string.photosbyseconds));
        resolucion.setText(camara.getResolucionHorizontal()+"x"+camara.getResolucionVertical());
        longitudSensor.setText(camara.getLongitudHorizontalSensor()+"mm X "+camara.getLongitudVerticalSensor()+"mm");
        return v;
    }
}
