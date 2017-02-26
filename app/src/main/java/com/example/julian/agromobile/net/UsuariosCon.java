package com.example.julian.agromobile.net;

import android.content.Context;
import android.widget.Toast;

import com.example.julian.agromobile.models.Usuario;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.List;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by Julian on 20/05/2015.
 */
public class UsuariosCon {

    MobileServiceClient client;
    Context con;
    List<Usuario> result;
    public UsuariosCon(UsuarioConI usuarioConI,Context context){
        con = context;
        this.usuarioConI=usuarioConI;
        try {
            client=new MobileServiceClient("https://mobileagroapp.azurewebsites.net",context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public interface UsuarioConI{
        public void onReadCompleted(List<Usuario> result);
        public void onRegisterCompleted();
    }

    UsuarioConI usuarioConI;
    public void insert(Usuario item){
        ListenableFuture<Usuario> result = getTable().insert(item);
        Futures.addCallback(result, new FutureCallback<Usuario>() {
            @Override
            public void onFailure(Throwable exc) {
                Toast.makeText(con, exc.getCause().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Usuario result) {
                Toast.makeText(con, "Registro Completado"+ result.getNombre(), Toast.LENGTH_SHORT).show();
                usuarioConI.onRegisterCompleted();
                //TODO COLOCAR UN MENSAJE DE ERROR EL CUAL DIGA QUE EXISTIÓ UN ERROR EN EL REGISTRO
            }
        });
    }
    public void update(Usuario item){

        getTable().update(item);

    }
    public void delete(Usuario item){

        getTable().delete(item);
    }

    public MobileServiceTable<Usuario> getTable(){
        return client.getTable(Usuario.class);
    }

    public void getAllUsers(){
        try {
            ListenableFuture<MobileServiceList<Usuario>> result= getTable().where().field("complete").eq(val(false)).execute();
            Futures.addCallback(result, new FutureCallback<List<Usuario>>() {
                @Override
                public void onFailure(Throwable exc) {
                    Toast.makeText(con, exc.getCause().toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(List<Usuario> result) {
                    //Toast.makeText(con, "Busqueda Completada con "+ result.size(), Toast.LENGTH_SHORT).show();
                    usuarioConI.onReadCompleted(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Class<Usuario> getClassModel() {
        return  Usuario.class;
    }

}
