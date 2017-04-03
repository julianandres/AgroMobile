package com.example.julian.agromobile;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.julian.agromobile.adapters.SubProcessAdapter;
import com.example.julian.agromobile.models.Proceso;
import com.example.julian.agromobile.models.SubProceso;
import com.example.julian.agromobile.net.ProcesosCon;
import com.example.julian.agromobile.net.SubProcesosCon;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ProcessActivity extends AppCompatActivity implements ProcesosCon.ProcesoConI, SubProcesosCon.SubProcesoConI, AdapterView.OnItemClickListener {

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
        subProcessList.setOnItemClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //TODO IMPLEMETAR UN ALERT DIALOG PARA DETALLES DE SUBPROCESO


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        finish();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onReadProcessCompleted(List<Proceso> result) {
         if(result!=null){
             proceso=result.get(0);
             getSupportActionBar().setTitle(R.string.txt_process);
             nombre.setText(proceso.getNombre());
             fechaInicio.setText(getString(R.string.txt_init_on)+formatDate(proceso.getFechaInicio()));
             fechaFin.setText(getString(R.string.txt_finish_on)+formatDate(proceso.getFechaFin()));
             if(!proceso.isState()){
                 estado.setText(R.string.finish);
             }else{
                 estado.setText(R.string.on_process);
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
            SubProceso sp = result.get(i);
            GregorianCalendar fechaHoy = new GregorianCalendar();
            GregorianCalendar config = new GregorianCalendar();
            fechaHoy.setTime(new Date());
            Date datePrevia = addDays(sp.getFecha(), -1);
            Date datePost = addDays(sp.getFecha(), 1);
            config.setTime(datePrevia);
            System.out.println(fechaHoy.getTime() + " fechaHoy");
            System.out.println(config.getTime() + " fecha config");
            int ts = config.compareTo(fechaHoy);
            if (ts < 0) {
                config.setTime(datePost);
                ts = config.compareTo(fechaHoy);
                if (ts > 0) {
                    //esta en el rango disponible
                    if (sp.getEstado() == 0) {
                        sp.setEstado(1);//estado disponible
                    } else {
                        sp.setEstado(2);//estado en proceso
                    }

                } else {
                    //rango pasado

                    if (sp.getEstado() == 0) {
                        sp.setEstado(4);//no se subieron fotos
                    } else {
                        sp.setEstado(3);//se subieron fotos
                    }
                }
            } else {
                sp.setEstado(0);
                //aun no se llega la fecha
            }
            dataSubProcess.add(result.get(i));
        }
        subProcessAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRegisterSubProcessCompleted() {

    }

    String formatDate(Date imput){
        String fecha="";
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        fecha =formatoFecha.format(imput)+" a las "+formatoHora.format(imput);
        return fecha;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String mensaje ="";
        if(dataSubProcess.get(position).getEstado()==0){
            mensaje =getString(R.string.txt_estara_available);
        }else {
            if (dataSubProcess.get(position).getEstado() == 1) {
                mensaje = getString(R.string.input_to_sistemweb);
            }else{
                if (dataSubProcess.get(position).getEstado() == 2) {
                    mensaje = getString(R.string.already_upload_);
                }else{
                    if (dataSubProcess.get(position).getEstado() == 3) {
                        mensaje = getString(R.string.information_upload_correcltly);
                    }else{
                        mensaje = getString(R.string.process_lost);
                    }
                }


            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje);
        builder.setTitle(dataSubProcess.get(position).getNombre());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).create().show();
    }
    public Date addDays(Date fecha, int dias) {
        Date dato = fecha;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        gc.add(Calendar.DAY_OF_MONTH, dias);
        dato = gc.getTime();
        return dato;
    }
}
