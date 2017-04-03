package com.example.julian.agromobile.net;

import android.content.Context;
import android.widget.Toast;

import com.example.julian.agromobile.models.Aeronave;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.List;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by JULIAN on 27/02/2017.
 */

public class AeronavesCon {
    MobileServiceClient client;
    Context con;
    List<Aeronave> result;
    AeronaveConI AeronaveConI;

    public AeronavesCon(AeronaveConI AeronaveConI, Context context) {
        con = context;
        this.AeronaveConI = AeronaveConI;
        try {
            client = new MobileServiceClient("https://mobileagroapp.azurewebsites.net", context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Aeronave item) {
        ListenableFuture<Aeronave> result = getTable().insert(item);
        Futures.addCallback(result, new FutureCallback<Aeronave>() {
            @Override
            public void onFailure(Throwable exc) {
                Toast.makeText(con, exc.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Aeronave result) {
                // Toast.makeText(con, "Registro Completado" + result.getNombre(), Toast.LENGTH_SHORT).show();
                AeronaveConI.onRegisterAircraftCompleted();
                //TODO COLOCAR UN MENSAJE DE ERROR EL CUAL DIGA QUE EXISTIÓ UN ERROR EN EL REGISTRO
            }
        });
    }

    public void update(Aeronave item) {
        ListenableFuture<Aeronave> result= getTable().update(item);
        Futures.addCallback(result, new FutureCallback<Aeronave>() {
            @Override
            public void onFailure(Throwable exc) {
                Toast.makeText(con, exc.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Aeronave result) {
                // Toast.makeText(con, "Registro Completado" + result.getNombre(), Toast.LENGTH_SHORT).show();
                AeronaveConI.onUpdateAircraftCompleted();
                //TOD O COLOCAR UN MENSAJE DE ERROR EL CUAL DIGA QUE EXISTIÓ UN ERROR EN EL REGISTRO
            }
        });
    }

    public void delete(Aeronave item) {

        ListenableFuture<Void> result=getTable().delete(item);
        Futures.addCallback(result, new FutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AeronaveConI.onDeleteAircraftCompleted();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(con, throwable.getCause().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public MobileServiceTable<Aeronave> getTable() {
        return client.getTable(Aeronave.class);
    }

    public void getAllAircrafts() {
        try {
            ListenableFuture<MobileServiceList<Aeronave>> result = getTable().where().field("complete").eq(val(false)).execute();
            Futures.addCallback(result, new FutureCallback<List<Aeronave>>() {
                @Override
                public void onFailure(Throwable exc) {
                    Toast.makeText(con, exc.getCause().toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(List<Aeronave> result) {
                    //Toast.makeText(con, "Busqueda Completada con " + result.size(), Toast.LENGTH_SHORT).show();
                    AeronaveConI.onReadAircraftCompleted(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void getAircraftByIdUser(String idUser ){
        try {
            ListenableFuture<MobileServiceList<Aeronave>> result= getTable().where().field("idUsuario").eq(val(idUser)).execute();
            Futures.addCallback(result, new FutureCallback<List<Aeronave>>() {
                @Override
                public void onFailure(Throwable exc) {
                    Toast.makeText(con, exc.getCause().toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(List<Aeronave> result) {
                   // Toast.makeText(con, "Busqueda de Aeronaves con "+ result.size(), Toast.LENGTH_SHORT).show();
                    AeronaveConI.onReadAircraftCompleted(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<Aeronave> getClassModel() {
        return Aeronave.class;
    }

    public interface AeronaveConI {
        public void onReadAircraftCompleted(List<Aeronave> result);
        public void onRegisterAircraftCompleted();
        public void onUpdateAircraftCompleted();
        public void onDeleteAircraftCompleted();
    }



}
