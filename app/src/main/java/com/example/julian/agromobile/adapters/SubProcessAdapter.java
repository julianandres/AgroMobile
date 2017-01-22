package com.example.julian.agromobile.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julian.agromobile.R;
import com.example.julian.agromobile.models.SubProceso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by JULIAN on 11/12/2016.
 */

public class SubProcessAdapter extends BaseAdapter {


    Context context;
    List<SubProceso> data;

    public SubProcessAdapter(Context context, List<SubProceso> data) {
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
            v = View.inflate(context, R.layout.template_sub_process, null);
        else
            v = convertView;

         SubProceso subProceso = (SubProceso) getItem(position);

        TextView txt = (TextView) v.findViewById(R.id.template_subprocess_title);
        txt.setText(subProceso.getNombre());
        txt = (TextView) v.findViewById(R.id.template_subprocess_date);
        txt.setText(formatDate(subProceso.getFecha()));

        ImageView imgBackground = (ImageView) v.findViewById(R.id.template_background_img);
        switch (subProceso.getEstado()){
            case 0:{
                imgBackground.setBackgroundResource(R.color.subProcessNoState);
            } break;
            case 1:{
                imgBackground.setBackgroundResource(R.color.subProcessActualState);
            }break;
            case 2:{
                imgBackground.setBackgroundResource(R.color.subProcessCompleteState);
            }break;
        }
        return v;
    }
    String formatDate(Date imput){
        String fecha="";
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        fecha =formatoFecha.format(imput)+" a las "+formatoHora.format(imput);
        return fecha;
    }
}
