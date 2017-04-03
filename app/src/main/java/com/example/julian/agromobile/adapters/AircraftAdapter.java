package com.example.julian.agromobile.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.julian.agromobile.R;
import com.example.julian.agromobile.models.Aeronave;
import com.example.julian.agromobile.models.SubProceso;

import java.util.List;

/**
 * Created by JULIAN on 26/02/2017.
 */

public class AircraftAdapter extends BaseAdapter {
    Context context;
    List<Aeronave> data;

    public AircraftAdapter(Context context, List<Aeronave> data) {
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
            v = View.inflate(context, R.layout.template_aircrafts, null);
        else
            v = convertView;

        Aeronave aeronave= (Aeronave) getItem(position);
        TextView autonomia = (TextView) v.findViewById(R.id.template_aircraft_autonomy);
        TextView velCrucero = (TextView) v.findViewById(R.id.velocidad_crucero);
        TextView alturaMaxima = (TextView) v.findViewById(R.id.altura_maxima);
        TextView ref = (TextView) v.findViewById(R.id.template_aircraft_ref);
        alturaMaxima.setText(aeronave.getAltura() + context.getString(R.string.metters));
        velCrucero.setText(aeronave.getVelocidadCrucero() + " m/s");
        autonomia.setText(aeronave.getAutonomia()+context.getString(R.string.txt_minutes));
        ref.setText(aeronave.getReferencia());
        return v;
    }
}
