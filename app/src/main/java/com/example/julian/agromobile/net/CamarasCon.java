package com.example.julian.agromobile.net;

import android.content.Context;
import android.widget.Toast;

import com.example.julian.agromobile.models.Camara;
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
 * Created by JULIAN on 02/03/2017.
 */

public class CamarasCon {

    MobileServiceClient client;
    Context con;
    List<Camara> result;
    CamaraConI camaraConI;

    public CamarasCon(CamaraConI camaraConI, Context context) {
        con = context;
        this.camaraConI = camaraConI;
        try {
            client = new MobileServiceClient("https://mobileagroapp.azurewebsites.net", context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Camara item) {
        ListenableFuture<Camara> result = getTable().insert(item);
        Futures.addCallback(result, new FutureCallback<Camara>() {
            @Override
            public void onFailure(Throwable exc) {
                Toast.makeText(con, exc.toString(), Toast.LENGTH_LONG).show();
                //TODO COLOCAR UN MENSAJE DE ERROR EL CUAL DIGA QUE EXISTIÓ UN ERROR EN EL REGISTRO
            }

            @Override
            public void onSuccess(Camara result) {
                // Toast.makeText(con, "Registro Completado" + result.getNombre(), Toast.LENGTH_SHORT).show();
                camaraConI.onRegisterCameraCompleted();

            }
        });
    }

    public void update(Camara item) {
        ListenableFuture<Camara> result= getTable().update(item);
        Futures.addCallback(result, new FutureCallback<Camara>() {
            @Override
            public void onFailure(Throwable exc) {
                Toast.makeText(con, exc.toString(), Toast.LENGTH_LONG).show();
                //TODO COLOCAR UN MENSAJE DE ERROR EL CUAL DIGA QUE EXISTIÓ UN ERROR EN EL REGISTRO
            }

            @Override
            public void onSuccess(Camara result) {
                // Toast.makeText(con, "Registro Completado" + result.getNombre(), Toast.LENGTH_SHORT).show();
                camaraConI.onUpdateCameraCompleted();

            }
        });
    }

    public void delete(Camara item) {

        ListenableFuture<Void> result=getTable().delete(item);
        Futures.addCallback(result, new FutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                camaraConI.onDeleteCameraCompleted();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(con, throwable.getCause().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public MobileServiceTable<Camara> getTable() {
        return client.getTable(Camara.class);
    }

    public void getAllCameras() {
        try {
            ListenableFuture<MobileServiceList<Camara>> result = getTable().execute();
            Futures.addCallback(result, new FutureCallback<List<Camara>>() {
                @Override
                public void onFailure(Throwable exc) {
                    System.out.println("error verify");
                   // Toast.makeText(con, exc.getCause().toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(List<Camara> result) {
                   // Toast.makeText(con, "Busqueda Completada con " + result.size(), Toast.LENGTH_SHORT).show();
                    camaraConI.onReadCameraCompleted(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void getCameraByIdUser(String idUser ){
        try {
            ListenableFuture<MobileServiceList<Camara>> result= getTable().where().field("idUsuario").eq(val(idUser)).execute();
            Futures.addCallback(result, new FutureCallback<List<Camara>>() {
                @Override
                public void onFailure(Throwable exc) {
                    Toast.makeText(con, exc.getCause().toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(List<Camara> result) {
                   // Toast.makeText(con, "Busqueda de Camaras con "+ result.size(), Toast.LENGTH_SHORT).show();
                    camaraConI.onReadCameraCompleted(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface CamaraConI {
        public void onReadCameraCompleted(List<Camara> result);
        public void onRegisterCameraCompleted();
        public void onUpdateCameraCompleted();
        public void onDeleteCameraCompleted();
    }

}
