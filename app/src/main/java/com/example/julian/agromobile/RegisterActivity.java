package com.example.julian.agromobile;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.julian.agromobile.models.Usuario;
import com.example.julian.agromobile.net.UsuariosCon;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, UsuariosCon.UsuarioConI {

    EditText nombre,email,login,password,confPassword;
    Button btnAceptar;
    List<Usuario> usuarios;
    UsuariosCon usercon;
    boolean azureLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nombre = (EditText) findViewById(R.id.txt_name);
        email = (EditText) findViewById(R.id.txt_email);
        login = (EditText) findViewById(R.id.txt_email);
        password = (EditText) findViewById(R.id.txt_pass);
        confPassword = (EditText) findViewById(R.id.txt_confpass);
        btnAceptar = (Button) findViewById(R.id.button_register);
        btnAceptar.setOnClickListener(this);
        btnAceptar.setEnabled(false);
        //TODO MEJORAR LA PRESENTACIÓN DEL REGISTER
        usuarios = new ArrayList<Usuario>();
        azureLogin = false;
        usercon = new UsuariosCon(this, this);
        usercon.getAllUsers();
        getSupportActionBar().setTitle(R.string.txt_register);

        
    }

    @Override
    public void onClick(View v) {
        boolean val = false;
        boolean arroba = false;
        if(azureLogin) {
            if (!email.getText().toString().equals("") || !password.getText().toString().equals("") ||
                    !confPassword.getText().toString().equals("") || !nombre.getText().toString().equals("")
                    || !login.getText().toString().equals("")) {
                for (int i = 0; i < email.getText().toString().length(); i++) {
                    String a = email.getText().toString().substring(i, i + 1);
                    if (a.equals("@")) {
                        arroba = true;
                    }
                }
                if (arroba) {

                    if (password.getText().toString().equals(confPassword.getText().toString())) {
                        val = true;
                    } else {
                        val = false;
                    }
                    if (val) {
                        for (int i = 0; i < usuarios.size(); i++) {

                            if (usuarios.get(i).getLogin().equals(login.getText().toString())) {
                                val = false;
                            }
                            // MessageBox.Show(login.Text + "      " + usuarios.ElementAt(i).Login);
                        }
                        if (val) {
                            Usuario us = new Usuario();
                            us.setPassword(password.getText().toString());
                            us.setEmail(email.getText().toString());
                            us.setLogin(login.getText().toString());
                            us.setNombre(nombre.getText().toString());
                            us.setTipo("cliente");
                            usercon.insert(us);
                            //usercon.getAllUsers();
                            //mongo.insertDocument(us,this);
                            //mongo.findAllDocuments(this);
                            // NavigationService.Navigate(new Uri("/IniciarSesion.xaml", UriKind.Relative));
                        } else {
                            Toast.makeText(this, R.string.existent, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.password_diferent, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.wrong_email, Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(this, R.string.verify_fields, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, R.string.wrong_network, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onReadCompleted(List<Usuario> result) {
        usuarios.clear();
        btnAceptar.setEnabled(true);
        azureLogin = true;
        for(int i=0;i<result.size();i++){
            usuarios.add(result.get(i));
        }
    }

    @Override
    public void onRegisterCompleted() {
        finish();
    }
}
