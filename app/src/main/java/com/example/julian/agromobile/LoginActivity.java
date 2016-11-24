package com.example.julian.agromobile;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, UsuariosCon.UsuarioConI {

    static final String KEY_USER="userId";
    static final String KEY_USER_NAME="userName";
    static final String KEY_USER_MAIL="userMail";

    EditText login,password;
    Button btnAceptar;
    UsuariosCon usuariosCon;
    List<Usuario> usuarios;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private MobileServiceClient mClient;
    private MobileServiceTable<Usuario> mUserTable;
    boolean azureLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login= (EditText) findViewById(R.id.txt_login);
        password= (EditText) findViewById(R.id.txt_password);
        btnAceptar = (Button) findViewById(R.id.btn_aceptar);
        usuarios= new ArrayList<Usuario>();
        btnAceptar.setOnClickListener(this);
        usuariosCon=new UsuariosCon(this,this);
        //usuariosCon.getAllUsers();
        preferences= getSharedPreferences("pref",MODE_PRIVATE);
        editor=preferences.edit();
        try {
            mClient = new MobileServiceClient(
                    "https://agromovil.azurewebsites.net",
                    this);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mUserTable = mClient.getTable(Usuario.class);
        refreshItemsFromTable();
    }
    @Override
    protected void onResume() {
        //usuariosCon.getAllUsers();
        refreshItemsFromTable();
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        //usuariosCon.getAllUsers();
        refreshItemsFromTable();
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(usuarios.size()==0)
        {
            //usuariosCon.getAllUsers();
            refreshItemsFromTable();
        }
        Usuario us= new Usuario();
        boolean validate=false;
        for(int i=0;i<usuarios.size();i++){
            if(!validate){
                if(usuarios.get(i).getLogin().equals(login.getText().toString())){
                    if(usuarios.get(i).getPassword().equals(password.getText().toString())){
                        validate=true;
                        us.setNombre(usuarios.get(i).getNombre());
                        us.setLogin(usuarios.get(i).getLogin());
                        us.setEmail(usuarios.get(i).getEmail());
                        us.setPassword(usuarios.get(i).getPassword());
                    }else{
                        validate=false;
                    }
                }else{
                    validate=false;
                }
            }
        }
        if(azureLogin) {
            if (validate) {
                //TODO shared preferences
                editor.putString(KEY_USER, us.getLogin());
                editor.putString(KEY_USER_NAME, us.getNombre());
                editor.putString(KEY_USER_MAIL,us.getEmail());
                editor.commit();
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                editor.putString(KEY_USER, "-1");
                editor.commit();
                Toast.makeText(this, R.string.ErrorLoginPasswIncorrect, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, R.string.Red_ISSUE, Toast.LENGTH_SHORT).show();
        }
        String usLog=preferences.getString(LoginActivity.KEY_USER,"-1");

    }

    @Override
    public void onReadCompleted(List<Usuario> result, int count, Exception exception, ServiceFilterResponse response) {
        usuarios.clear();
        for(int i=0;i<result.size();i++){
            usuarios.add(result.get(i));
        }
    }

    @Override
    public void onDeleteComplete(Exception exception, ServiceFilterResponse response) {

    }

    @Override
    public void onComlete(Usuario entity, Exception exception, ServiceFilterResponse response) {

    }
    private void refreshItemsFromTable() {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<Usuario> results = refreshItemsFromMobileServiceTable();
                    usuarios.clear();
                    for(int i=0;i<results.size();i++){
                        usuarios.add(results.get(i));
                    }
                    if(usuarios.size()>0)
                    azureLogin = true;


                    System.out.println("hola123456");

                } catch (Exception e){

                }

                return null;
            }
        };

        runAsyncTask(task);
    }
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private List<Usuario> refreshItemsFromMobileServiceTable()  {

        List<Usuario> result= new ArrayList<Usuario>();
        try {
            result = mUserTable.where().field("complete").
                    eq(val(false)).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("hola");
        return result;
    }


}
