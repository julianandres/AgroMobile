package com.example.julian.agromobile;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julian.agromobile.models.Aeronave;
import com.example.julian.agromobile.net.AeronavesCon;

import java.util.ArrayList;
import java.util.List;

public class AddUAVActivity extends AppCompatActivity implements View.OnClickListener, AeronavesCon.AeronaveConI {

    TextView autonomy,altura,speed,referencia;
    Button guardarUav;
    AeronavesCon aeronavesCon;
    List<Aeronave> aeronaves;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String usuarioLogin;
    String idUsuario;
    boolean azureLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_uav);
        Bundle extras = getIntent().getExtras();
        idUsuario=extras.getString(LoginActivity.KEY_USER_NAME);
        preferences= getSharedPreferences("pref",MODE_PRIVATE);
        editor=preferences.edit();
        usuarioLogin=preferences.getString(LoginActivity.KEY_USER,"-1");
        autonomy = (TextView) findViewById(R.id.txt_autonomy_aeronave);
        altura= (TextView) findViewById(R.id.txt_autonomy_aeronave);
        speed = (TextView) findViewById(R.id.txt_speed_aircraft);
        referencia = (TextView) findViewById(R.id.txt_ref_aeronave);
        guardarUav = (Button) findViewById(R.id.button_add_uav);
        guardarUav.setOnClickListener(this);
        aeronavesCon=new AeronavesCon(this,this);
        aeronaves = new ArrayList<Aeronave>();
        aeronavesCon.getAircraftByIdUser(usuarioLogin);
        guardarUav.setEnabled(false);


    }

    @Override
    public void onClick(View v) {
        if(azureLogin) {
            if (!autonomy.getText().toString().equals("") || !altura.getText().toString().equals("") ||
                    !referencia.getText().toString().equals("") || !speed.getText().toString().equals("")
                   ) {

                            Aeronave aeronave = new Aeronave();
                            aeronave.setAutonomia(Integer.parseInt(autonomy.getText().toString()));
                            aeronave.setReferencia(referencia.getText().toString());
                            aeronave.setVelocidadCrucero(Integer.parseInt(speed.getText().toString()));
                            aeronave.setAltura(Integer.parseInt(altura.getText().toString()));
                            aeronave.setIdUsuario(idUsuario);
                            aeronavesCon.insert(aeronave);
                            //usercon.getAllUsers();
                            //mongo.insertDocument(us,this);
                            //mongo.findAllDocuments(this);
                            // NavigationService.Navigate(new Uri("/IniciarSesion.xaml", UriKind.Relative));

                } else {
                Toast.makeText(this, R.string.verify_fields, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, R.string.wrong_network, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onReadAircraftCompleted(List<Aeronave> result) {
        aeronaves.clear();
        azureLogin=true;
        guardarUav.setEnabled(true);
        for(int i=0;i<result.size();i++){
            aeronaves.add(result.get(i));
        }
    }

    @Override
    public void onRegisterAircraftCompleted() {
            finish();
    }

    @Override
    public void onUpdateAircraftCompleted() {

    }

    @Override
    public void onDeleteAircraftCompleted() {

    }
}
