package com.example.julian.agromobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.julian.agromobile.models.Proceso;
import com.example.julian.agromobile.models.SubProceso;
import com.example.julian.agromobile.net.SubProcesosCon;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class NewProcessActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, SubProcesosCon.SubProcesoConI {


    SubProcesosCon subProcesosCon;
    private RadioGroup rdgGrupo;
    private int recurrency;
    private EditText nombre;
    private EditText duracionSemanas;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_process);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rdgGrupo = (RadioGroup)findViewById(R.id.recurrence);
        rdgGrupo.setOnCheckedChangeListener(this);
        nombre = (EditText) findViewById(R.id.txtNameProccess);
        duracionSemanas = (EditText) findViewById(R.id.durationWeek);
        btnSave = (Button) findViewById(R.id.btn_save_process);
        btnSave.setOnClickListener(this);
        subProcesosCon = new SubProcesosCon(this, this);
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rdbTwo) {
            recurrency=2;
        }else if (checkedId == R.id.rdbThree){
            recurrency = 3;
        } else if (checkedId == R.id.rdbFourth) {
            recurrency = 4;
        }
    }

    @Override
    public void onClick(View v) {
        Proceso proceso = new Proceso();
        Date fechaInicio = new Date();
        Date fechaFin = new Date();

        int duracionSemanasEntero = Integer.parseInt(duracionSemanas.getText().toString());
        int duracionDias = duracionSemanasEntero * 7;

        proceso.setNombre(nombre.getText().toString());

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

        SubProceso subProceso1 = new SubProceso();
        SubProceso subProceso2 = new SubProceso();
        SubProceso subProceso3 = new SubProceso();
        SubProceso subProceso4 = new SubProceso();
        subProceso1.setNumeroenProceso(1);
        subProceso2.setNumeroenProceso(1);
        subProceso3.setNumeroenProceso(1);
        subProceso4.setNumeroenProceso(1);
        subProceso1.setNombre("subProceso 1 de " + proceso.getNombre());
        subProceso2.setNombre("subProceso 2 de " + proceso.getNombre());
        subProceso3.setNombre("subProceso 3 de " + proceso.getNombre());
        subProceso4.setNombre("subProceso 4 de " + proceso.getNombre());
        subProceso1.setIdProceso(proceso.getNombre());
        subProceso2.setIdProceso(proceso.getNombre());
        subProceso3.setIdProceso(proceso.getNombre());
        subProceso4.setIdProceso(proceso.getNombre());

        switch (recurrency) {
            case 2: {
                subProceso1.setFecha(addDays(fechadeHoy, 1));
                subProceso2.setFecha(addDays(fechadeHoy, duracionDias));
                break;
            }
            case 3: {
                subProceso1.setFecha(addDays(fechadeHoy, 1));
                subProceso2.setFecha(addDays(fechadeHoy, duracionDias / 2));
                subProceso3.setFecha(addDays(fechadeHoy, duracionDias));
                break;
            }
            case 4: {
                subProceso1.setFecha(addDays(fechadeHoy, 1));
                subProceso2.setFecha(addDays(fechadeHoy, duracionDias / 3));
                subProceso3.setFecha(addDays(fechadeHoy, 2 * duracionDias / 3));
                subProceso4.setFecha(addDays(fechadeHoy, duracionDias));
            }
            break;
        }
        subProcesosCon.insert(subProceso1);
    }

    public Date addDays(Date fecha, int dias) {
        Date dato = fecha;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        gc.add(Calendar.DAY_OF_MONTH, dias);
        dato = gc.getTime();
        return dato;
    }

    @Override
    public void onReadCompleted(List<SubProceso> result, int count, Exception exception, ServiceFilterResponse response) {

    }

    @Override
    public void onDeleteComplete(Exception exception, ServiceFilterResponse response) {

    }

    @Override
    public void onComlete(SubProceso entity, Exception exception, ServiceFilterResponse response) {
        System.out.println("onCompleteFinish");
    }
}
