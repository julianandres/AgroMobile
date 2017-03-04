package com.example.julian.agromobile;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julian.agromobile.models.Camara;
import com.example.julian.agromobile.net.CamarasCon;

import java.util.ArrayList;
import java.util.List;

public class AddCameraActivity extends AppCompatActivity implements View.OnClickListener, CamarasCon.CamaraConI {

    EditText  refCamara;
    EditText  longitudFocal;
    EditText  velocidadCaptura; //fotos/segundo
    EditText  resolucionHorizontal;
    EditText  resolucionVertical;
    EditText longitudHorizontalSensor;
    EditText  longitudVerticalSensor;
    Button guardarCamara;
    CamarasCon camarasCon;
    List<Camara> camaras;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String usuarioLogin;
    String idUsuario;
    boolean azureLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera);
        Bundle extras = getIntent().getExtras();
        idUsuario=extras.getString(LoginActivity.KEY_USER_NAME);
        preferences= getSharedPreferences("pref",MODE_PRIVATE);
        editor=preferences.edit();
        usuarioLogin=preferences.getString(LoginActivity.KEY_USER,"-1");

        refCamara = (EditText) findViewById(R.id.ref_camera);
        longitudFocal = (EditText) findViewById(R.id.focal_length);
        velocidadCaptura = (EditText) findViewById(R.id.velocidad_captura); //fotos/segundo
        resolucionHorizontal = (EditText) findViewById(R.id.txt_resol_horizontal);
        resolucionVertical = (EditText) findViewById(R.id.txt_resol_vertical);
        longitudHorizontalSensor = (EditText) findViewById(R.id.longitud_horiz_sensor);
        longitudVerticalSensor = (EditText) findViewById(R.id.longitud_vert_sensor);
        guardarCamara = (Button) findViewById(R.id.button_add_camera);
        guardarCamara.setOnClickListener(this);
        camarasCon=new CamarasCon(this,this);
        camaras = new ArrayList<Camara>();
        camarasCon.getCameraByIdUser(usuarioLogin);
        guardarCamara.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if(azureLogin) {
            if (!refCamara.getText().toString().equals("") || !longitudFocal.getText().toString().equals("") ||
                    !velocidadCaptura.getText().toString().equals("") || !resolucionHorizontal.getText().toString().equals("")
                    || !resolucionVertical.getText().toString().equals("")|| !longitudHorizontalSensor.getText().toString().equals("")
                    || !longitudVerticalSensor.getText().toString().equals("")
                    ) {

                    Camara camara = new Camara();
                try {
                    camara.setRefCamara(refCamara.getText().toString());
                    camara.setLongitudFocal(Float.parseFloat(longitudFocal.getText().toString()));
                    camara.setVelocidadCaptura(Integer.parseInt(velocidadCaptura.getText().toString()));
                    camara.setResolucionHorizontal(Integer.parseInt(resolucionHorizontal.getText().toString()));
                    camara.setResolucionVertical(Integer.parseInt(resolucionVertical.getText().toString()));
                    camara.setLongitudHorizontalSensor(Float.parseFloat(longitudHorizontalSensor.getText().toString()));
                    camara.setLongitudVerticalSensor(Float.parseFloat(longitudVerticalSensor.getText().toString()));
                    camarasCon.insert(camara);
                }catch (Exception ex){
                    Toast.makeText(this,"Error de datos",Toast.LENGTH_SHORT);
                }
                //usercon.getAllUsers();
                //mongo.insertDocument(us,this);
                //mongo.findAllDocuments(this);
                // NavigationService.Navigate(new Uri("/IniciarSesion.xaml", UriKind.Relative));

            } else {
                Toast.makeText(this, "Verifique, Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Error de Red", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onReadCameraCompleted(List<Camara> result) {
        camaras.clear();
        azureLogin=true;
        guardarCamara.setEnabled(true);

        for(int i=0;i<result.size();i++){
            camaras.add(result.get(i));
        }
    }

    @Override
    public void onRegisterCameraCompleted() {
        finish();
    }

    @Override
    public void onUpdateCameraCompleted() {

    }

    @Override
    public void onDeleteCameraCompleted() {

    }
}
