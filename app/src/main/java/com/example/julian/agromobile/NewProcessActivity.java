package com.example.julian.agromobile;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.julian.agromobile.adapters.AircraftAdapter;
import com.example.julian.agromobile.models.Aeronave;
import com.example.julian.agromobile.models.Proceso;
import com.example.julian.agromobile.models.SubProceso;
import com.example.julian.agromobile.net.AeronavesCon;
import com.example.julian.agromobile.net.ProcesosCon;
import com.example.julian.agromobile.net.SubProcesosCon;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class NewProcessActivity extends AppCompatActivity implements SubProcesosCon.SubProcesoConI, ProcesosCon.ProcesoConI, RadioGroup.OnCheckedChangeListener, View.OnClickListener, AeronavesCon.AeronaveConI {


    SubProcesosCon subProcesosCon;
    ProcesosCon procesosCon;
    private RadioGroup rdgGrupo;
    private int recurrency;
    private EditText nombre;
    private EditText duracionSemanas;
    private Button btnSave;
    int contadoSubProcesosRegistrados;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String usLog;
    private Proceso proceso;
    List<Aeronave> aeronaves;
    ListView listViewAeronaves;
    AeronavesCon aeronavesCon;
    AircraftAdapter aircraftAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_process);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nuevo Proceso");

        listViewAeronaves = (ListView) findViewById(R.id.list_aeronaves_process);
        aeronaves = new ArrayList<>();
        aircraftAdapter = new AircraftAdapter(this,aeronaves);
        listViewAeronaves.setAdapter(aircraftAdapter);

        rdgGrupo = (RadioGroup)findViewById(R.id.recurrence);
        rdgGrupo.setOnCheckedChangeListener(this);
        nombre = (EditText) findViewById(R.id.txtNameProccess);
        duracionSemanas = (EditText) findViewById(R.id.durationWeek);
        btnSave = (Button) findViewById(R.id.btn_save_process);
        btnSave.setOnClickListener(this);
        subProcesosCon = new SubProcesosCon(this, this);
        aeronavesCon = new AeronavesCon(this,this);
        procesosCon = new ProcesosCon(this,this);
        contadoSubProcesosRegistrados=0;
        preferences= getSharedPreferences("pref",MODE_PRIVATE);
        editor=preferences.edit();
        usLog = preferences.getString(LoginActivity.KEY_USER,"-1");
        aeronavesCon.getAircraftByIdUser(usLog);
        recurrency=0;
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rdbTwo) {
            recurrency = 2;
        }else if (checkedId == R.id.rdbThree){
            recurrency = 3;
        } else if (checkedId == R.id.rdbFourth) {
            recurrency = 4;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_next_aircraft) {
            if (!nombre.getText().toString().equals("") && !duracionSemanas.getText().toString().equals("") && recurrency != 0) {
                proceso = new Proceso();
                Date fechaInicio = new Date();
                Date fechaFin = new Date();

                int duracionSemanasEntero = Integer.parseInt(duracionSemanas.getText().toString());
                int duracionDias = duracionSemanasEntero * 7;

                proceso.setNombre(nombre.getText().toString());
                Date imput;
                imput = fechaInicio;
                String fecha = "";
                DateFormat formatoHora = new SimpleDateFormat("HHmmss");
                DateFormat formatoFecha = new SimpleDateFormat("ddMMyyyy");

                fecha = formatoFecha.format(imput) + "" + formatoHora.format(imput);
                System.out.println(fecha);
                proceso.setId(fecha);
                Date fechadeHoy = new Date();
                GregorianCalendar fechaHoy = new GregorianCalendar();
                GregorianCalendar config = new GregorianCalendar();
                fechaHoy.setTime(fechadeHoy);

                proceso.setFechaInicio(fechadeHoy);
                proceso.setFechaFin(addDays(fechadeHoy, duracionDias));

                proceso.setDuracionSemanas(duracionSemanasEntero);

                proceso.setState(true);
                proceso.setNumeroSubprocesos(recurrency);
                proceso.setSubProcesoActual(1);
                proceso.setIdUsuario(usLog);
                SubProceso subProceso1 = new SubProceso();
                SubProceso subProceso2 = new SubProceso();
                SubProceso subProceso3 = new SubProceso();
                SubProceso subProceso4 = new SubProceso();
                subProceso1.setNumeroenProceso(1);
                subProceso2.setNumeroenProceso(2);
                subProceso3.setNumeroenProceso(3);
                subProceso4.setNumeroenProceso(4);
                subProceso1.setNombre("subProceso 1 de " + proceso.getNombre());
                subProceso2.setNombre("subProceso 2 de " + proceso.getNombre());
                subProceso3.setNombre("subProceso 3 de " + proceso.getNombre());
                subProceso4.setNombre("subProceso 4 de " + proceso.getNombre());
                subProceso1.setIdProceso(proceso.getId());
                subProceso2.setIdProceso(proceso.getId());
                subProceso3.setIdProceso(proceso.getId());
                subProceso4.setIdProceso(proceso.getId());
                subProceso1.setEstado(0);
                subProceso2.setEstado(0);
                subProceso3.setEstado(0);
                subProceso4.setEstado(0);
                subProceso1.setFotonoir(0);
                subProceso2.setFotonoir(0);
                subProceso3.setFotonoir(0);
                subProceso4.setFotonoir(0);
                subProceso1.setFotorgb(0);
                subProceso2.setFotorgb(0);
                subProceso3.setFotorgb(0);
                subProceso4.setFotorgb(0);
                switch (recurrency) {
                    case 2: {
                        subProceso1.setFecha(addDays(fechadeHoy, 1));
                        subProceso2.setFecha(addDays(fechadeHoy, duracionDias));
                        subProcesosCon.insert(subProceso1);

                        subProcesosCon.insert(subProceso2);

                        break;
                    }
                    case 3: {
                        subProceso1.setFecha(addDays(fechadeHoy, 1));
                        subProceso2.setFecha(addDays(fechadeHoy, duracionDias / 2));
                        subProceso3.setFecha(addDays(fechadeHoy, duracionDias));
                        subProcesosCon.insert(subProceso1);
                        subProcesosCon.insert(subProceso2);
                        subProcesosCon.insert(subProceso3);
                        break;
                    }
                    case 4: {
                        subProceso1.setFecha(addDays(fechadeHoy, 1));
                        subProceso2.setFecha(addDays(fechadeHoy, duracionDias / 3));
                        subProceso3.setFecha(addDays(fechadeHoy, 2 * duracionDias / 3));
                        subProceso4.setFecha(addDays(fechadeHoy, duracionDias));
                        subProcesosCon.insert(subProceso1);
                        subProcesosCon.insert(subProceso2);
                        subProcesosCon.insert(subProceso3);
                        subProcesosCon.insert(subProceso4);
                    }
                    break;
                }
                //TODO CREAR ALERT DIALOG PARA CONFIRMAR FECHAS Y NOMBRES
                //TODO COLOCAR LOS CALENDARIOS
                //TODO REALIZAR LAS VALIDACIONES CORRESPONDIENTES
            } else {
                Toast.makeText(this, R.string.error_fields, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Date addDays(Date fecha, int dias) {
        Date dato = fecha;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeZone(TimeZone.getTimeZone("UTC-05:00"));
        gc.setTime(fecha);
        gc.add(Calendar.DAY_OF_MONTH, dias);
        dato = gc.getTime();

        return dato;
    }

    @Override
    public void onReadProcessCompleted(List<Proceso> result) {

    }
    @Override
    public void onRegisterProcessCompleted() {
        finish();
    }

    @Override
    public void onReadSubProcessCompleted(List<SubProceso> result) {

    }

    @Override
    public void onRegisterSubProcessCompleted() {
        contadoSubProcesosRegistrados++;
        if(recurrency==contadoSubProcesosRegistrados){
            System.out.println("sub proceso numero "+contadoSubProcesosRegistrados);
            procesosCon.insert(proceso);
        }
    }

    @Override
    public void onReadAircraftCompleted(List<Aeronave> result) {
        aeronaves.clear();
        if(result.size()<1){
            System.out.println(result.size());
            // emptyView.setVisibility(View.GONE);
            //aeronaves.setVisibility(View.INVISIBLE);
            System.out.println("visible");
        }
        for(int i=0;i<result.size();i++){
            aeronaves.add(result.get(i));
        }
        aircraftAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRegisterAircraftCompleted() {

    }

    @Override
    public void onUpdateAircraftCompleted() {

    }

    @Override
    public void onDeleteAircraftCompleted() {

    }
}
