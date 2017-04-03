package com.example.julian.agromobile;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julian.agromobile.adapters.AircraftAdapter;
import com.example.julian.agromobile.adapters.CameraAdapter;
import com.example.julian.agromobile.models.Aeronave;
import com.example.julian.agromobile.models.Camara;
import com.example.julian.agromobile.models.Proceso;
import com.example.julian.agromobile.models.SubProceso;
import com.example.julian.agromobile.net.AeronavesCon;
import com.example.julian.agromobile.net.CamarasCon;
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

public class NewProcessActivity extends AppCompatActivity implements SubProcesosCon.SubProcesoConI, ProcesosCon.ProcesoConI, RadioGroup.OnCheckedChangeListener, View.OnClickListener, AeronavesCon.AeronaveConI, CamarasCon.CamaraConI, AdapterView.OnItemClickListener {


    SubProcesosCon subProcesosCon;
    ProcesosCon procesosCon;
    CamarasCon camarasCon;
    View viewPrevio;
    private RadioGroup rdgGrupo;
    private int recurrency;
    private EditText nombre;
    private EditText duracionSemanas;
    private Button btnSave,btnNextAircraft,btnNextCamera;
    int contadoSubProcesosRegistrados;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String usLog;
    private Proceso proceso;
    List<Aeronave> aeronaves;
    List<Camara> camaras;
    ListView listViewAeronaves,listViewCamaras;
    AeronavesCon aeronavesCon;
    AircraftAdapter aircraftAdapter;
    CameraAdapter cameraAdapter;
    SubProceso subProceso1;
    SubProceso subProceso2;
    SubProceso subProceso3;
    SubProceso subProceso4;
    TextView headerView;
    LinearLayout linear1;
    RelativeLayout linear2,linear3;
    int estado;
    FloatingActionButton fabCameras,fabAeronaves;
    Aeronave aeronaveSeleccionada;
    Camara camaraSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_process);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALENDAR)!= PackageManager.PERMISSION_GRANTED) {
            System.out.println("holaaaaaaaaaaaaaaaaaaaaaaaaaaassssssssssssssssssss");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CALENDAR)) {} else { ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    1);
            }
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CALENDAR)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CALENDAR)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        1);
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.txt_new_process);

        listViewAeronaves = (ListView) findViewById(R.id.list_aeronaves_process);
        listViewCamaras= (ListView) findViewById(R.id.list_camera_process);
        fabAeronaves= (FloatingActionButton) findViewById(R.id.fab_aircrafts_process);
        fabCameras= (FloatingActionButton) findViewById(R.id.fab_cameras_process);
        headerView = (TextView) findViewById(R.id.header_np);
        fabAeronaves.setOnClickListener(this);
        fabCameras.setOnClickListener(this);
        aeronaves = new ArrayList<Aeronave>();
        camaras = new ArrayList<Camara>();
        aircraftAdapter = new AircraftAdapter(this,aeronaves);
        cameraAdapter = new CameraAdapter(this,camaras);
        listViewAeronaves.setAdapter(aircraftAdapter);
        listViewCamaras.setAdapter(cameraAdapter);
        listViewCamaras.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        rdgGrupo = (RadioGroup)findViewById(R.id.recurrence);
        rdgGrupo.setOnCheckedChangeListener(this);
        nombre = (EditText) findViewById(R.id.txtNameProccess);
        duracionSemanas = (EditText) findViewById(R.id.durationWeek);
        btnSave = (Button) findViewById(R.id.btn_save_process);
        btnNextAircraft = (Button) findViewById(R.id.btn_next_aircraft);
        btnNextCamera = (Button) findViewById(R.id.btn_next_camera);
        btnNextCamera.setOnClickListener(this);
        btnNextAircraft.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        subProcesosCon = new SubProcesosCon(this, this);
        aeronavesCon = new AeronavesCon(this,this);
        camarasCon = new CamarasCon(this,this);
        procesosCon = new ProcesosCon(this,this);
        aeronavesCon.getAircraftByIdUser(usLog);
        contadoSubProcesosRegistrados=0;
        listViewCamaras.setOnItemClickListener(this);
        listViewAeronaves.setOnItemClickListener(this);
        camarasCon.getAllCameras();
        preferences= getSharedPreferences("pref",MODE_PRIVATE);
        editor=preferences.edit();
        linear1= (LinearLayout) findViewById(R.id.linear1);
        linear2= (RelativeLayout) findViewById(R.id.linear2);
        linear3= (RelativeLayout) findViewById(R.id.linear3);
        usLog = preferences.getString(LoginActivity.KEY_USER,"-1");
        aeronavesCon.getAircraftByIdUser(usLog);
        recurrency=0;
        estado=0;
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        aeronavesCon.getAircraftByIdUser(usLog);
        camarasCon.getAllCameras();
        super.onResume();
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
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(rdgGrupo.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fab_aircrafts_process){
            Intent intent = new Intent(this, AddUAVActivity.class);
            intent.putExtra(LoginActivity.KEY_USER_NAME,usLog);
            startActivity(intent);
        }
        if (v.getId()==R.id.fab_cameras_process){
            Intent intent = new Intent(this, AddCameraActivity.class);
            intent.putExtra(LoginActivity.KEY_USER_NAME, usLog);
            startActivity(intent);
        }
        
        
        //region
        if(v.getId()==R.id.btn_next_aircraft){
            headerView.setText(R.string.txt_step2of3);
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
                subProceso1 = new SubProceso();
                subProceso2 = new SubProceso();
                subProceso3 = new SubProceso();
                subProceso4 = new SubProceso();
                subProceso1.setNumeroenProceso(1);
                subProceso2.setNumeroenProceso(2);
                subProceso3.setNumeroenProceso(3);
                subProceso4.setNumeroenProceso(4);
                subProceso1.setNombre(getString(R.string.txt_task) + proceso.getNombre());
                subProceso2.setNombre(getString(R.string.txt_task2) + proceso.getNombre());
                subProceso3.setNombre(getString(R.string.task_3) + proceso.getNombre());
                subProceso4.setNombre(getString(R.string.task_4) + proceso.getNombre());
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
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.VISIBLE);
                estado=1;
                getSupportActionBar().setTitle(R.string.txt_add_aeronaves);
                System.out.println("estadoooooooooooooo");
                //TODO CREAR ALERT DIALOG PARA CONFIRMAR FECHAS Y NOMBRES
                //TODO COLOCAR LOS CALENDARIOS
                //TODO REALIZAR LAS VALIDACIONES CORRESPONDIENTES
            } else {
                Toast.makeText(this, R.string.error_fields, Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId()==R.id.btn_next_camera){
            headerView.setText(R.string.txt_process_3_step_3);
            System.out.println("holaaaaa");
            if(aeronaveSeleccionada!=null) {
                linear2.setVisibility(View.GONE);
                linear3.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle(R.string.add_cameras_proces);
                viewPrevio=null;
                estado = 2;
            }else{
                Toast.makeText(this, R.string.txt_select_aircraft,Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId()==R.id.btn_save_process) {

            if (camaraSeleccionada != null) {
                estado=3;
                switch (recurrency) {
                    case 2: {
                        addEvent(subProceso1.getId(),subProceso1);
                        addEvent(subProceso2.getId(),subProceso2);
                        subProcesosCon.insert(subProceso1);
                        subProcesosCon.insert(subProceso2);

                        break;
                    }
                    case 3: {
                        addEvent(subProceso1.getId(),subProceso1);
                        addEvent(subProceso2.getId(),subProceso2);
                        addEvent(subProceso3.getId(),subProceso3);
                        subProcesosCon.insert(subProceso1);
                        subProcesosCon.insert(subProceso2);
                        subProcesosCon.insert(subProceso3);

                        break;
                    }
                    case 4: {
                        addEvent(subProceso1.getId(),subProceso1);
                        addEvent(subProceso2.getId(),subProceso2);
                        addEvent(subProceso3.getId(),subProceso3);
                        addEvent(subProceso4.getId(),subProceso4);
                        subProcesosCon.insert(subProceso1);
                        subProcesosCon.insert(subProceso2);
                        subProcesosCon.insert(subProceso3);
                        subProcesosCon.insert(subProceso4);

                    }
                    break;
                }
                proceso.setIdAeronave("idaeronave");
                //TODO implementar las id del avion y la cámara
            }else{
                Toast.makeText(this, R.string.select_a_camera,Toast.LENGTH_SHORT).show();
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

    @Override
    public void onReadCameraCompleted(List<Camara> result) {
        System.out.println("holaaa");
        camaras.clear();
        if (result.size() < 1) {
            System.out.println(result.size());

            System.out.println("visible");
        }
        for (int i = 0; i < result.size(); i++) {
            camaras.add(result.get(i));

            // camaras.setVisibility(View.GONE);
            System.out.println("invisible");
        }
        cameraAdapter.notifyDataSetChanged();


    }

    @Override
    public void onRegisterCameraCompleted() {

    }

    @Override
    public void onUpdateCameraCompleted() {

    }

    @Override
    public void onDeleteCameraCompleted() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int template = R.id.linear_template_aeronave;
        if(estado==1){
            aeronaveSeleccionada= (Aeronave) aircraftAdapter.getItem(position);
        }

        if(estado==2){
            template = R.id.linear_template_camara;
            camaraSeleccionada=(Camara)cameraAdapter.getItem(position);
        }

        if(viewPrevio==null){
            viewPrevio=view;
        }else{
            LinearLayout linear2 = (LinearLayout) viewPrevio.findViewById(template);
            linear2.setBackgroundResource(R.color.nav_item_icon);
        }
        LinearLayout linear= (LinearLayout) view.findViewById(template);
        linear.setBackgroundResource(R.color.accent);
        viewPrevio=view;
    }
    private void addEvent(String m_selectedCalendarId, SubProceso subProceso) {

            ContentValues l_event = new ContentValues();
            l_event.put("calendar_id", "1");
            l_event.put("title", getString(R.string.recordatory_title)+proceso.getNombre());
            l_event.put("description", getString(R.string.thisisarecordatory) + subProceso.getNombre());
            l_event.put("eventLocation", "@home");//Por ver o quitar
            l_event.put(CalendarContract.Events.DTSTART, subProceso.getFecha().getTime());
            l_event.put(CalendarContract.Events.DTEND, subProceso.getFecha().getTime() + 1800 * 1000);
            l_event.put("allDay", 0);
            l_event.put("eventTimezone", TimeZone.getDefault().getID());
            //status: 0~ tentative; 1~ confirmed; 2~ canceled
            l_event.put("eventStatus", 1);
            //0~ default; 1~ confidential; 2~ private; 3~ public
            // l_event.put("visibility", 0);
            //0~ opaque, no timing conflict is allowed; 1~ transparency, allow overlap of scheduling
            // l_event.put("transparency", 1);
            //0~ false; 1~ true
            l_event.put("hasAlarm", 1);
            Uri l_eventUri;
            if (Build.VERSION.SDK_INT >= 8) {
                l_eventUri = Uri.parse("content://com.android.calendar/events");
            } else {
                l_eventUri = Uri.parse("content://calendar/events");
            }
            Uri l_uri = this.getContentResolver().insert(l_eventUri, l_event);
            Log.v("++++++test", l_uri.toString());

    }
}
