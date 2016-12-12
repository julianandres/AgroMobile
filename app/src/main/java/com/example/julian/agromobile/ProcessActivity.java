package com.example.julian.agromobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.julian.agromobile.adapters.SubProcessAdapter;
import com.example.julian.agromobile.models.Proceso;
import com.example.julian.agromobile.models.SubProceso;
import com.example.julian.agromobile.net.ProcesosCon;
import com.example.julian.agromobile.net.SubProcesosCon;

import java.util.ArrayList;
import java.util.List;

public class ProcessActivity extends AppCompatActivity implements ProcesosCon.ProcesoConI, SubProcesosCon.SubProcesoConI {

    public static final String KEY_ID = "keyid" ;

    TextView nombre,fechaInicio,fechaFin,estado;
    ListView subProcessList;
    String id;
    ProcesosCon procesosCon;
    SubProcesosCon subProcesosCon;
    Proceso proceso;
    SubProcessAdapter subProcessAdapter;
    List<SubProceso> dataSubProcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        Bundle extras = getIntent().getExtras();
        id = extras.getString(KEY_ID);
        subProcessList= (ListView) findViewById(R.id.listSubProcess);
        nombre = (TextView) findViewById(R.id.name_process);
        fechaInicio = (TextView) findViewById(R.id.date_init_process);
        fechaFin = (TextView) findViewById(R.id.date_fin_process);
        estado = (TextView) findViewById(R.id.state_process);
        procesosCon = new ProcesosCon(this,this);
        procesosCon.getProcessById(id);
        subProcesosCon = new SubProcesosCon(this,this);
        subProcesosCon.getProcessByIdProcess(id);
        proceso = new Proceso();
        dataSubProcess = new ArrayList<SubProceso>();
        subProcessAdapter = new SubProcessAdapter(getApplication(),dataSubProcess);
        subProcessList.setAdapter(subProcessAdapter);
        //TODO IMPLEMETAR TODO LO DE LA ACTIVIDAD DEL PROCESO
    }

    @Override
    public void onReadProcessCompleted(List<Proceso> result) {
         if(result!=null){
             proceso=result.get(0);
             nombre.setText(proceso.getNombre());
             fechaInicio.setText(proceso.getFechaInicio().toString());
             fechaFin.setText(proceso.getFechaFin().toString());
             if(!proceso.isState()){
                 estado.setText("Finalizado");
             }else{
                 estado.setText("En proceso");
             }


         }
    }

    @Override
    public void onRegisterProcessCompleted() {

    }

    @Override
    public void onReadSubProcessCompleted(List<SubProceso> result) {
        dataSubProcess.clear();
        for(int i=0;i<result.size();i++){
            dataSubProcess.add(result.get(i));
        }
        subProcessAdapter.notifyDataSetChanged();
    }
    @Override
    public void onRegisterSubProcessCompleted() {

    }
}
