package com.example.julian.agromobile.net;

import android.content.Context;
import android.widget.Toast;

import com.example.julian.agromobile.models.Proceso;
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
 * Created by JULIAN on 07/12/2016.
 */

public class ProcesosCon {


    MobileServiceClient client;
    Context con;
    List<Proceso> result;
    public ProcesosCon(ProcesoConI ProcesoConI,Context context){
        con = context;
        this.ProcesoConI=ProcesoConI;
        try {
            client=new MobileServiceClient("https://agromobile.azurewebsites.net",context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public interface ProcesoConI{
        public void onReadCompleted(List<Proceso> result);
        public void onRegisterCompleted();
    }

    ProcesoConI ProcesoConI;
    public void insert(Proceso item){
        ListenableFuture<Proceso> result = getTable().insert(item);
        Futures.addCallback(result, new FutureCallback<Proceso>() {
            @Override
            public void onFailure(Throwable exc) {
                Toast.makeText(con, exc.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Proceso result) {
                Toast.makeText(con, "Registro Completado"+ result.getNombre(), Toast.LENGTH_SHORT).show();
                ProcesoConI.onRegisterCompleted();
                //TODO COLOCAR UN MENSAJE DE ERROR EL CUAL DIGA QUE EXISTIÓ UN ERROR EN EL REGISTRO
            }
        });
    }
    public void update(Proceso item){

        getTable().update(item);

    }
    public void delete(Proceso item){

        getTable().delete(item);
    }

    public MobileServiceTable<Proceso> getTable(){
        return client.getTable(Proceso.class);
    }

    public void getAllUsers(){
        try {
            ListenableFuture<MobileServiceList<Proceso>> result= getTable().where().field("complete").eq(val(false)).execute();
            Futures.addCallback(result, new FutureCallback<List<Proceso>>() {
                @Override
                public void onFailure(Throwable exc) {
                    Toast.makeText(con, exc.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(List<Proceso> result) {
                    Toast.makeText(con, "Busqueda Completada con "+ result.size(), Toast.LENGTH_SHORT).show();
                    ProcesoConI.onReadCompleted(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Class<Proceso> getClassModel() {
        return  Proceso.class;
    }

}
